package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class RefreshTokenTokenRequestValidationStrategy implements TokenRequestValidationStrategy {

    @Override
    public boolean apply(TokenRequest request) {
        return GrantTypeUtils.REFRESH_TOKEN.equals(request.getGrant_type());
    }

    @Override
    public void validate(TokenRequest request, Errors errors) {
        if (StringUtils.isEmpty(request.getRefresh_token())) {
            errors.rejectValue("refresh_token", "refresh_token.required", "refresh token is required");
        }
    }
}
