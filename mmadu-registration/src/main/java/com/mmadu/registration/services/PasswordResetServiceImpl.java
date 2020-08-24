package com.mmadu.registration.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmadu.event.bus.providers.EventPublisher;
import com.mmadu.registration.events.PasswordResetInitiationEvent;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.PasswordResetFlowConfiguration;
import com.mmadu.registration.models.PasswordResetRequestConfirmForm;
import com.mmadu.registration.models.PasswordResetRequestForm;
import com.mmadu.registration.models.otp.Otp;
import com.mmadu.registration.models.otp.OtpGenerationRequest;
import com.mmadu.registration.models.otp.OtpValidationRequest;
import com.mmadu.registration.providers.otp.OtpService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    private DomainFlowConfigurationService domainFlowConfigurationService;
    private WebClient userServiceClient;
    private OtpService otpService;
    private String baseUrl;
    private EventPublisher eventPublisher;
    private MmaduUserServiceClient mmaduUserServiceClient;

    @Autowired
    public void setDomainFlowConfigurationService(DomainFlowConfigurationService domainFlowConfigurationService) {
        this.domainFlowConfigurationService = domainFlowConfigurationService;
    }

    @Autowired
    @Qualifier("userService")
    public void setUserServiceClient(WebClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Autowired
    public void setOtpService(OtpService otpService) {
        this.otpService = otpService;
    }

    @Value("${mmadu.base-url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Autowired
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setMmaduUserServiceClient(MmaduUserServiceClient mmaduUserServiceClient) {
        this.mmaduUserServiceClient = mmaduUserServiceClient;
    }

    @Override
    public void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm) {
        log.info("Resetting password for domain {} {}", domainId, requestForm);
        final PasswordResetFlowConfiguration config = getPasswordResetFlowConfiguration(domainId);
        Flux.fromIterable(config.getUserFields())
                .map(propertyName -> this.convertToQuery(propertyName, requestForm))
                .flatMap(query -> this.mmaduUserServiceClient.queryForSingleUser(domainId, query))
                .take(1)
                .flatMap(userId -> this.createPasswordRequestTokenForUser(domainId, userId, config))
                .subscribe();
    }

    private PasswordResetFlowConfiguration getPasswordResetFlowConfiguration(String domainId) {
        PasswordResetFlowConfiguration configuration = domainFlowConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new).getPasswordReset();
        if (configuration == null) {
            configuration = new PasswordResetFlowConfiguration();
        }
        return configuration;
    }

    private String convertToQuery(String property, PasswordResetRequestForm form) {
        return String.format("%s equals '%s'", property, form.getUser());
    }

    private Mono<Void> createPasswordRequestTokenForUser(String domainId, String userId,
                                                         PasswordResetFlowConfiguration configuration) {
        log.info("Creating password request token for user {}", userId);
        String key = String.format("%s|%s", userId, "email");
        return otpService.generateOtp(OtpGenerationRequest.builder()
                .domainId(domainId)
                .key(key)
                .profile(configuration.getOtpProfile())
                .build()
        ).map(otp -> generateResetLinkFromOtp(otp, userId, domainId, configuration))
                .flatMap(link -> notifyPasswordResetInitiation(domainId, userId, link, configuration));
    }

    private String generateResetLinkFromOtp(Otp otp, String userId, String domainId,
                                            PasswordResetFlowConfiguration configuration) {
        if(StringUtils.isEmpty(configuration.getConfirmationFormUrl())) {
            return String.format("%s/%s/passwordReset/confirm?id=%s&token=%s&user=%s",
                    StringUtils.isEmpty(configuration.getLinkPasswordConfirmationBaseUrl()) ?
                            baseUrl : configuration.getLinkPasswordConfirmationBaseUrl(), domainId, otp.getId(), otp.getValue(), userId);
        } else {
            return String.format("%s?id=%s&token=%s&user=%s",
                    configuration.getLinkPasswordConfirmationUrl(), otp.getId(), otp.getValue(), userId);
        }
    }

    private Mono<Void> notifyPasswordResetInitiation(String domainId, String userId, String link,
                                                     PasswordResetFlowConfiguration configuration) {
        PasswordResetInitiationEvent event = new PasswordResetInitiationEvent();
        event.setDomain(domainId);
        event.setUserId(userId);
        event.setResetLink(link);
        event.setId(UUID.randomUUID().toString());
        return eventPublisher.publishEvent(Mono.just(event));
    }

    @Override
    public void confirmPasswordReset(String domainId,
                                     PasswordResetRequestConfirmForm requestForm) {
        log.info("Performing reset for {} {}", domainId, requestForm);
        final PasswordResetFlowConfiguration config = getPasswordResetFlowConfiguration(domainId);
        boolean valid = otpService.validateOtp(OtpValidationRequest.builder()
                .domainId(domainId)
                .key(String.format("%s|%s", requestForm.getUserId(), "email"))
                .profile(config.getOtpProfile())
                .otpId(requestForm.getOtpId())
                .value(requestForm.getOtpValue())
                .build()).blockOptional().orElse(false);
        if (!valid) {
            throw new IllegalArgumentException("invalid OTP");
        }
        mmaduUserServiceClient.updateUserPassword(domainId, requestForm.getUserId(), requestForm.getNewPassword());
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserResponse {
        private List<User> content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private String id;
    }
}
