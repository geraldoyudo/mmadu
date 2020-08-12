package com.mmadu.registration.validators;

import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.providers.propertyvalidation.PropertyValidatorResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PropertyValidationRequestValidator implements Validator {
    private PropertyValidatorResolver resolver;

    @Autowired
    public void setResolver(PropertyValidatorResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ValidationRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationRequest request = (ValidationRequest) o;
        resolver.getValidatorForType(request.getValidationType())
                .ifPresentOrElse(v -> v.validateRequest(request, errors),
                        () -> errors.rejectValue("type", "type.not.supported"));
    }
}
