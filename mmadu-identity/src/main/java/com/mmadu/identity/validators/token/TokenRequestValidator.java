package com.mmadu.identity.validators.token;

import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.validators.token.strategies.TokenRequestValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class TokenRequestValidator implements Validator {
    private List<TokenRequestValidationStrategy> strategies;

    @Autowired(required = false)
    public void setStrategies(List<TokenRequestValidationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TokenRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TokenRequest request = (TokenRequest) o;
        strategies.stream()
                .filter(s -> s.apply(request))
                .forEach(s -> s.validate(request, errors));
    }
}
