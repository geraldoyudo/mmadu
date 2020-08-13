package com.mmadu.registration.providers.propertyvalidation;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import org.springframework.validation.Errors;

import java.util.Map;

public interface PropertyValidator {

    String type();

    void validateRequest(ValidationRequest request, Errors errors);

    Map<String, Object> prepareForValidation(ValidationRequest request, String userProperty);

    boolean validate(ValidationAttempt attempt, Map<String, Object> context);

    void validateAttempt(ValidationAttempt request, Errors errors);
}
