package com.mmadu.identity.exceptions;

public class ClientInstanceNotFoundException extends RuntimeException {
    public ClientInstanceNotFoundException() {
    }

    public ClientInstanceNotFoundException(String message) {
        super(message);
    }
}
