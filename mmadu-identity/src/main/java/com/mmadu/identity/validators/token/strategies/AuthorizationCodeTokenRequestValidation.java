package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.models.token.TokenRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class AuthorizationCodeTokenRequestValidation implements TokenRequestValidationStrategy {
    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public boolean apply(TokenRequest request) {
        return GRANT_TYPE.equals(request.getGrant_type());
    }

    @Override
    public void validate(TokenRequest request, Errors errors) {
        if (StringUtils.isEmpty(request.getCode())) {
            errors.rejectValue("code", "code.required", "code is required");
        }
    }
}
