package com.mmadu.identity.validators.authorization;

import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.validators.authorization.responsestrategies.AuthorizationResponseValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Component
public class AuthorizationResponseValidator implements Validator {
    private List<AuthorizationResponseValidationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<AuthorizationResponseValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AuthorizationResponse.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AuthorizationResponse response = (AuthorizationResponse) o;
        strategies.stream()
                .filter(s -> s.apply(response))
                .forEach(s -> s.validate(response, errors));
    }
}
