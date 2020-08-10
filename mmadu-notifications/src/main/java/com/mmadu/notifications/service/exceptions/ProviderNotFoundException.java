package com.mmadu.notifications.service.exceptions;

public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException() {
    }

    public ProviderNotFoundException(String message) {
        super(message);
    }

    public ProviderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
