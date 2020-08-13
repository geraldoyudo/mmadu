package com.mmadu.registration.exceptions;

public class PropertyValueExpectationFailedException extends RuntimeException {
    public PropertyValueExpectationFailedException() {
    }

    public PropertyValueExpectationFailedException(String s) {
        super(s);
    }

    public PropertyValueExpectationFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PropertyValueExpectationFailedException(Throwable throwable) {
        super(throwable);
    }
}
