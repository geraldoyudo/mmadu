package com.mmadu.registration.exceptions;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException() {
    }

    public DomainNotFoundException(String s) {
        super(s);
    }
}
