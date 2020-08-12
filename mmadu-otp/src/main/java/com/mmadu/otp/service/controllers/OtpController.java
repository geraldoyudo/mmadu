package com.mmadu.otp.service.controllers;

import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;
import com.mmadu.otp.service.models.OtpValidationResponse;
import com.mmadu.otp.service.services.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/otp")
public class OtpController {
    private OtpService otpService;

    @Autowired
    public void setOtpService(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/token")
    @PreAuthorize("hasAuthority('otp.token_generate')")
    public OtpToken generateToken(@RequestBody @Valid OtpGenerationRequest request) {
        return otpService.generateOTP(request);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasAuthority('otp.token_validate')")
    public OtpValidationResponse validateToken(@RequestBody @Valid OtpValidationRequest request) {
        return new OtpValidationResponse(otpService.validateOTP(request));
    }
}
