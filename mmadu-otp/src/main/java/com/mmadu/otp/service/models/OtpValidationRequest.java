package com.mmadu.otp.service.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
public class OtpValidationRequest {
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "otpId is required")
    private String otpId;
    @NotEmpty(message = "value is required")
    private String value;
    @NotEmpty(message = "key is required")
    private String key;
    @NotEmpty(message = "profile is required")
    private String profile;
}
