package com.mmadu.registration.exceptions;

public class PropertyValidatorNotSupportedException extends RuntimeException {
    public PropertyValidatorNotSupportedException() {
    }

    public PropertyValidatorNotSupportedException(String s) {
        super(s);
    }

    public PropertyValidatorNotSupportedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PropertyValidatorNotSupportedException(Throwable throwable) {
        super(throwable);
    }
}
