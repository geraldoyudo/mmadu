package com.mmadu.identity.validators.token.errorsstrategies;

import com.mmadu.identity.models.token.error.InvalidGrant;
import com.mmadu.identity.models.token.error.TokenError;
import com.mmadu.identity.models.token.error.UnauthorizedClient;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedClientStrategy implements ValidationErrorProcessingStrategy {
    @Override
    public boolean apply(String field, String fieldErrorCode) {
        return "client_id".equals(field);
    }

    @Override
    public TokenError processError(String field, String fieldErrorCode) {
        UnauthorizedClient grant = new UnauthorizedClient();
        grant.setError_description(fieldErrorCode);
        return grant;
    }
}
