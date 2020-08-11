package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.exceptions.ProfileNotFoundException;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;
import com.mmadu.otp.service.providers.OtpProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OtpServiceImpl implements OtpService {
    private Map<String, OtpProvider> otpProviderMap = Collections.emptyMap();
    private OtpProfileService otpProfileService;

    @Autowired
    public void setOtpProfileService(OtpProfileService otpProfileService) {
        this.otpProfileService = otpProfileService;
    }

    @Autowired(required = false)
    public void setOtpProviders(List<OtpProvider> providers) {
        otpProviderMap = providers.stream()
                .collect(Collectors.toMap(OtpProvider::type, p -> p));
    }

    @Override
    public OtpToken generateOTP(OtpGenerationRequest request) {
        OtpProfile profile = otpProfileService.getOtpProfile(request.getDomainId(), request.getProfile())
                .orElseThrow(ProfileNotFoundException::new);
        return Optional.ofNullable(otpProviderMap.get(profile.getType()))
                .orElseThrow(ProfileNotFoundException::new).generateToken(request);
    }

    @Override
    public boolean validateOTP(OtpValidationRequest request) {
        OtpProfile profile = otpProfileService.getOtpProfile(request.getDomainId(), request.getProfile())
                .orElseThrow(ProfileNotFoundException::new);
        return Optional.ofNullable(otpProviderMap.get(profile.getType()))
                .orElseThrow(ProfileNotFoundException::new).validateToken(request);
    }
}
