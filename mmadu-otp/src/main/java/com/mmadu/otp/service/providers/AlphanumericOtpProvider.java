package com.mmadu.otp.service.providers;

import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.exceptions.ProfileNotFoundException;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;
import com.mmadu.otp.service.repositories.OtpTokenRepository;
import com.mmadu.otp.service.services.OtpProfileService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class AlphanumericOtpProvider implements OtpProvider {
    public static final String OTP_TYPE = "alphanumeric";
    private OtpProfileService otpProfileService;
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    public void setOtpProfileService(OtpProfileService otpProfileService) {
        this.otpProfileService = otpProfileService;
    }

    @Autowired
    public void setOtpTokenRepository(OtpTokenRepository otpTokenRepository) {
        this.otpTokenRepository = otpTokenRepository;
    }

    @Override
    public String type() {
        return OTP_TYPE;
    }

    @Override
    public OtpToken generateToken(OtpGenerationRequest request) {
        OtpProfile profile = otpProfileService.getOtpProfile(request.getDomainId(), request.getProfile())
                .orElseThrow(ProfileNotFoundException::new);
        OtpToken token = new OtpToken();
        token.setDomainId(request.getDomainId());
        token.setKey(request.getKey());
        token.setProfile(request.getProfile());
        token.setValue(RandomStringUtils.randomAlphanumeric(profile.getOtpLength()));
        token.setExpiryTime(ZonedDateTime.now().plus(profile.getOtpValidity().getValue(), profile.getOtpValidity().getTimeUnit()));
        token.setType(OTP_TYPE);
        token.setMaxAttempts(profile.getMaxAttempts());
        return otpTokenRepository.save(token);
    }

    @Override
    public boolean validateToken(OtpValidationRequest request) {
        Optional<OtpToken> token = otpTokenRepository.findById(request.getOtpId());

        if (token.isEmpty()) {
            return false;
        }
        OtpToken tokenValue = token.get();
        boolean attemptsExceeded = tokenValue.recordAttempt();
        boolean valid = tokenValue.getDomainId().equals(request.getDomainId()) &&
                tokenValue.getType().equals(OTP_TYPE) &&
                !tokenValue.hasExpired() &&
                tokenValue.getValue().equals(request.getValue()) &&
                tokenValue.getKey().equals(request.getKey());
        if (valid || attemptsExceeded) {
            otpTokenRepository.delete(tokenValue);
        } else {
            otpTokenRepository.save(tokenValue);
        }
        return valid;
    }
}
