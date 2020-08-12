package com.mmadu.registration.providers.otp;

import com.mmadu.registration.models.otp.Otp;
import com.mmadu.registration.models.otp.OtpGenerationRequest;
import com.mmadu.registration.models.otp.OtpValidationRequest;
import reactor.core.publisher.Mono;

public interface OtpService {

    Mono<Otp> generateOtp(OtpGenerationRequest request);

    Mono<Boolean> validateOtp(OtpValidationRequest request);
}
