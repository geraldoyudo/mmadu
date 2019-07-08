package com.mmadu.registration.exceptions;

public class FormFieldsGenerationException extends RuntimeException {
    public FormFieldsGenerationException() {
    }

    public FormFieldsGenerationException(String s) {
        super(s);
    }

    public FormFieldsGenerationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FormFieldsGenerationException(Throwable throwable) {
        super(throwable);
    }
}
