package com.mmadu.registration.models.propertyvalidation;

public class ValidationResponse {
    private boolean valid;

    public ValidationResponse() {
    }

    public ValidationResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
