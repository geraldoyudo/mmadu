package com.mmadu.identity.validators.token.errorsstrategies;

import com.mmadu.identity.models.token.error.InvalidGrant;
import com.mmadu.identity.models.token.error.TokenError;
import org.springframework.stereotype.Component;

@Component
public class InvalidGrantTypeStrategy implements ValidationErrorProcessingStrategy {
    @Override
    public boolean apply(String field, String fieldErrorCode) {
        return "grant_type".equals(field);
    }

    @Override
    public TokenError processError(String field, String fieldErrorCode) {
        InvalidGrant grant = new InvalidGrant();
        grant.setError_description(fieldErrorCode);
        return grant;
    }
}
