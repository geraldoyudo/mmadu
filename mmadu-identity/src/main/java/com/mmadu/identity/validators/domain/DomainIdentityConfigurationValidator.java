package com.mmadu.identity.validators.domain;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.validators.domain.configuration.DomainConfigurationValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Component
public class DomainIdentityConfigurationValidator implements Validator {
    private List<DomainConfigurationValidationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<DomainConfigurationValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return DomainIdentityConfiguration.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DomainIdentityConfiguration configuration = (DomainIdentityConfiguration) o;
        strategies.stream()
                .filter(s -> s.apply(configuration))
                .forEach(s -> s.validate(configuration, errors));
    }
}
