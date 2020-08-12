package com.mmadu.otp.service.providers;

import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;

public interface OtpProvider {

    String type();

    OtpToken generateToken(OtpGenerationRequest request);

    boolean validateToken(OtpValidationRequest request);
}
