package com.mmadu.identity.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class HasDomainValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return HasDomainValidator.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.info("Validating {}", o);
    }
}
