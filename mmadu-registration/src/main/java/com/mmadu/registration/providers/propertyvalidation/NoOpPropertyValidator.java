package com.mmadu.registration.providers.propertyvalidation;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Component
public class NoOpPropertyValidator implements PropertyValidator {

    @Override
    public String type() {
        return "none";
    }

    @Override
    public void validateRequest(ValidationRequest request, Errors errors) {
        // do nothing
    }

    @Override
    public Map<String, Object> prepareForValidation(ValidationRequest request, String userProperty) {
        return new HashMap<>();
    }

    @Override
    public boolean validate(ValidationAttempt attempt, Map<String, Object> context) {
        return true;
    }

    @Override
    public void validateAttempt(ValidationAttempt request, Errors errors) {
        // do nothing
    }
}
