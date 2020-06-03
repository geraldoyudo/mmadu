package com.mmadu.identity.exceptions;

public class CredentialCreationException extends RuntimeException {
    public CredentialCreationException() {
    }

    public CredentialCreationException(String message) {
        super(message);
    }

    public CredentialCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
