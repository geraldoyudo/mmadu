package com.mmadu.registration.providers.propertyvalidation;

import com.mmadu.event.bus.events.Event;
import com.mmadu.event.bus.providers.EventPublisher;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.exceptions.PropertyValueExpectationFailedException;
import com.mmadu.registration.models.PropertyValidationConfiguration;
import com.mmadu.registration.models.events.OTPPropertyValidationEvent;
import com.mmadu.registration.models.otp.Otp;
import com.mmadu.registration.models.otp.OtpGenerationRequest;
import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.providers.otp.OtpService;
import com.mmadu.registration.services.DomainFlowConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public abstract class OTPEventBasedPropertyValidator implements PropertyValidator {
    private OtpService otpService;
    private DomainFlowConfigurationService domainFlowConfigurationService;
    private EventPublisher eventPublisher;

    @Override
    public void validateRequest(ValidationRequest request, Errors errors) {

    }

    @Override
    public Map<String, Object> prepareForValidation(ValidationRequest request, String userProperty) {
        String propertyName = request.getPropertyName();
        if (StringUtils.isEmpty(userProperty)) {
            throw new PropertyValueExpectationFailedException(propertyName + " is empty");
        }
        Map<String, PropertyValidationConfiguration> configurationMap = domainFlowConfigurationService.findByDomainId(request.getDomainId())
                .orElseThrow(DomainNotFoundException::new)
                .getPropertyValidation();
        PropertyValidationConfiguration configuration = Optional.ofNullable(configurationMap).orElse(emptyMap())
                .getOrDefault(propertyName, new PropertyValidationConfiguration());
        Otp otp = otpService.generateOtp(OtpGenerationRequest.builder()
                .domainId(request.getDomainId())
                .profile(configuration.getOtpProfile())
                .key(String.format("%s|%s|%s", request.getUserId(), "property.validation", propertyName))
                .build()).block();
        eventPublisher.publishEvent(Mono.just(createEvent(otp, request, userProperty)))
                .subscribe();
        return Map.of(
                "value", otp.getValue(),
                propertyName, userProperty
        );
    }

    private Event createEvent(Otp otp, ValidationRequest request, String userProperty) {
        OTPPropertyValidationEvent event = new OTPPropertyValidationEvent();
        event.setType("validation.property." + request.getValidationType());
        event.setCode(otp.getValue());
        event.setDomain(request.getDomainId());
        event.setPropertyName(request.getPropertyName());
        event.setPropertyValue(userProperty);
        return event;
    }

    @Override
    public void validateAttempt(ValidationAttempt attempt, Errors errors) {
        Optional<String> code = attempt.get("code", String.class);
        if (code.isEmpty() || code.get().isBlank()) {
            errors.rejectValue("code", "code.required");
        }
    }

    @Override
    public boolean validate(ValidationAttempt attempt, Map<String, Object> context) {
        String code = attempt.get("code", String.class).orElse("");
        String value = (String) Optional.ofNullable(context.get("value")).orElseThrow(() -> new IllegalStateException("otp value not in context"));
        return value.equals(code);
    }

    @Autowired
    public void setOtpService(OtpService otpService) {
        this.otpService = otpService;
    }

    @Autowired
    public void setDomainFlowConfigurationService(DomainFlowConfigurationService domainFlowConfigurationService) {
        this.domainFlowConfigurationService = domainFlowConfigurationService;
    }

    @Autowired
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
