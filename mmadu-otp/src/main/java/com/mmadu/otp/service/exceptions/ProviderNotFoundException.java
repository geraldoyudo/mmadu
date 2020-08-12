package com.mmadu.otp.service.exceptions;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException() {
    }

    public ProviderNotFoundException(String message) {
        super(message);
    }
}
