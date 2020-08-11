package com.mmadu.otp.service.models;

public class OtpValidationResponse {
    private boolean valid;

    public OtpValidationResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }
}
