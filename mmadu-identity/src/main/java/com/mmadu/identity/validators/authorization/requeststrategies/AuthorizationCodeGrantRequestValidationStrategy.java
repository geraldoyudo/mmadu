package com.mmadu.identity.validators.authorization.requeststrategies;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AuthorizationCodeGrantRequestValidationStrategy implements AuthorizationRequestValidationStrategy {
    private String RESPONSE_TYPE = "code";

    @Override
    public boolean apply(AuthorizationRequest request) {
        return RESPONSE_TYPE.equals(request.getResponse_type());
    }

    @Override
    public void validate(AuthorizationRequest request, Errors errors) {

    }
}
