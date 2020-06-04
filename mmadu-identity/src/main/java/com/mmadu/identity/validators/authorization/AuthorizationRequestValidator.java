package com.mmadu.identity.validators.authorization;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.validators.authorization.requeststrategies.AuthorizationRequestValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Component
public class AuthorizationRequestValidator implements Validator {
    private List<AuthorizationRequestValidationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<AuthorizationRequestValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AuthorizationRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AuthorizationRequest request = (AuthorizationRequest) o;
        strategies.stream()
                .filter(s -> s.apply(request))
                .forEach(s -> s.validate(request, errors));
    }
}
