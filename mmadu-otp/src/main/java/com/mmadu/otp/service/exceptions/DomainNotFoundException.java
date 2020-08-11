package com.mmadu.otp.service.exceptions;

public class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException() {
    }

    public DomainNotFoundException(String message) {
        super(message);
    }
}
