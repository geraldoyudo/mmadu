package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.models.token.TokenRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class PasswordTokenRequestValidation implements TokenRequestValidationStrategy {
    private static final String GRANT_TYPE = "password";

    @Override
    public boolean apply(TokenRequest request) {
        return GRANT_TYPE.equals(request.getGrant_type());
    }

    @Override
    public void validate(TokenRequest request, Errors errors) {
        if (StringUtils.isEmpty(request.getUsername())) {
            errors.rejectValue("username", "username.required", "Username is required");
        }

        if (request.getPassword() == null) {
            errors.rejectValue("password", "password.required", "Password is required");
        }
    }
}
