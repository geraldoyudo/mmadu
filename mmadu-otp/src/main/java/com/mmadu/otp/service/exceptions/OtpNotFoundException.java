package com.mmadu.otp.service.exceptions;

public class OtpNotFoundException extends RuntimeException {
    public OtpNotFoundException() {
    }

    public OtpNotFoundException(String message) {
        super(message);
    }
}
