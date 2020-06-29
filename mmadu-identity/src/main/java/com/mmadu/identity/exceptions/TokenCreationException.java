package com.mmadu.identity.exceptions;

public class TokenCreationException extends RuntimeException {
    public TokenCreationException() {
    }

    public TokenCreationException(String message) {
        super(message);
    }

    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
