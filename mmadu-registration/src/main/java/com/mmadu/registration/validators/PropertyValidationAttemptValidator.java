package com.mmadu.registration.validators;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.providers.propertyvalidation.PropertyValidatorResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PropertyValidationAttemptValidator implements Validator {
    private PropertyValidatorResolver resolver;

    @Autowired
    public void setResolver(PropertyValidatorResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ValidationAttempt.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationAttempt attempt = (ValidationAttempt) o;
        resolver.getValidatorForType(attempt.getValidationType())
                .ifPresentOrElse(v -> v.validateAttempt(attempt, errors),
                        () -> errors.rejectValue("type", "type.not.supported"));
    }
}
