package com.mmadu.identity.exceptions;

import com.mmadu.identity.models.token.error.TokenError;

public class TokenException extends RuntimeException {
    private TokenError tokenError;

    public TokenException(TokenError tokenError) {
        super("token Error occurred: " + tokenError.getError() + ": " + tokenError.getError_description());
        this.tokenError = tokenError;
    }

    public TokenError getTokenError() {
        return tokenError;
    }
}
