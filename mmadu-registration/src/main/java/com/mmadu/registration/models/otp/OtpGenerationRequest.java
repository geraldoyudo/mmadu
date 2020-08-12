package com.mmadu.registration.models.otp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpGenerationRequest {
    private String domainId;
    private String key;
    private String profile;
}
