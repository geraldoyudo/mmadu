package com.mmadu.registration.exceptions;

public class RegistrationProfileNotFoundException extends RuntimeException {
    public RegistrationProfileNotFoundException() {
    }

    public RegistrationProfileNotFoundException(String s) {
        super(s);
    }
}
