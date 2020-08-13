package com.mmadu.service.models;

import javax.validation.constraints.NotEmpty;

public class PropertyValidationStateUpdateRequest {
    @NotEmpty(message = "propertyName is required")
    private String propertyName;
    private boolean valid;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
