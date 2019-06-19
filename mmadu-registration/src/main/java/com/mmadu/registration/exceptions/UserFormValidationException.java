package com.mmadu.registration.exceptions;

public class UserFormValidationException extends RuntimeException {
    public UserFormValidationException() {
    }

    public UserFormValidationException(String s) {
        super(s);
    }
}
