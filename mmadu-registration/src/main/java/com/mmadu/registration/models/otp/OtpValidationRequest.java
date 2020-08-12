package com.mmadu.registration.models.otp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpValidationRequest {
    private String domainId;
    private String otpId;
    private String value;
    private String key;
    private String profile;
}
