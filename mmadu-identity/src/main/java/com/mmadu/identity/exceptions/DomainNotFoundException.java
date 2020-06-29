package com.mmadu.identity.exceptions;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException() {
    }

    public DomainNotFoundException(String message) {
        super(message);
    }
}
