package com.mmadu.identity.exceptions;

public class CredentialNotFoundException extends RuntimeException {
    public CredentialNotFoundException() {
    }

    public CredentialNotFoundException(String message) {
        super(message);
    }
}
