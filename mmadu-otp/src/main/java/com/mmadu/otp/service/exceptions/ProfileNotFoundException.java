package com.mmadu.otp.service.exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException() {
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
