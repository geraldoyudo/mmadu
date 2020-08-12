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
import com.mmadu.registration.providers.otp.OtpService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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

    @Override
    public void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm) {
        log.info("Resetting password for domain {} {}", domainId, requestForm);
        PasswordResetFlowConfiguration configuration = domainFlowConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new).getPasswordReset();
        if (configuration == null) {
            configuration = new PasswordResetFlowConfiguration();
        }
        final PasswordResetFlowConfiguration config = configuration;
        Flux.fromIterable(configuration.getUserFields())
                .map(propertyName -> this.convertToQuery(propertyName, requestForm))
                .flatMap(query -> this.queryForSingleUser(domainId, query))
                .take(1)
                .flatMap(userId -> this.createPasswordRequestTokenForUser(domainId, userId, config))
                .subscribe();
    }

    private String convertToQuery(String property, PasswordResetRequestForm form) {
        return String.format("%s equals %s", property, form.getUser());
    }

    private Mono<String> queryForSingleUser(String domainId, String query) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder.path("/domains/")
                        .path(domainId)
                        .path("/users/search")
                        .queryParam("size", 2)
                        .queryParam("query", query)
                        .build()
                )
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .flatMap(this::ensureSingle);
    }

    private Mono<String> ensureSingle(UserResponse response) {
        if (response.getContent() == null || response.getContent().size() != 1) {
            return Mono.empty();
        } else {
            return Mono.just(response.getContent().get(0).getId());
        }
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
        ).map(otp -> generateResetLinkFromOtp(otp, userId, domainId))
                .flatMap(link -> notifyPasswordResetInitiation(domainId, userId, link, configuration));
    }

    private String generateResetLinkFromOtp(Otp otp, String userId, String domainId) {
        return String.format("%s/%s/passwordReset/confirm?id=%s&token=%s&user=%s",
                baseUrl, domainId, otp.getId(), otp.getValue(), userId);
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

    public void confirmPasswordReset(String domainId, PasswordResetRequestConfirmForm requestForm) {
        log.info("Performing reset for {} {}", domainId, requestForm);
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
