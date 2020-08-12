package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;

public interface OtpService {

    OtpToken generateOTP(OtpGenerationRequest request);

    boolean validateOTP(OtpValidationRequest request);
}
