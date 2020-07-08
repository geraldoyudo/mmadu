package com.mmadu.identity.exceptions;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
