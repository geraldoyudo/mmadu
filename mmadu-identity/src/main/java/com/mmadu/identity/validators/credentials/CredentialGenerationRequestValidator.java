package com.mmadu.identity.validators.credentials;

import com.mmadu.identity.models.security.CredentialGenerationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;

@Component
public class CredentialGenerationRequestValidator implements Validator {
    private List<CredentialRequestValidatingStrategy> strategies = Collections.emptyList();

    @Autowired
    public void setStrategies(List<CredentialRequestValidatingStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CredentialGenerationRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CredentialGenerationRequest request = (CredentialGenerationRequest) o;
        strategies.stream()
                .filter(s -> s.apply(request))
                .forEach(s -> s.validate(request, errors));
    }
}
