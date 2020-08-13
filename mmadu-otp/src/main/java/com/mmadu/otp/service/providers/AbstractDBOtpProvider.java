package com.mmadu.otp.service.providers;

import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.entities.OtpToken;
import com.mmadu.otp.service.exceptions.ProfileNotFoundException;
import com.mmadu.otp.service.models.OtpGenerationRequest;
import com.mmadu.otp.service.models.OtpValidationRequest;
import com.mmadu.otp.service.repositories.OtpTokenRepository;
import com.mmadu.otp.service.services.OtpProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.Optional;

public abstract class AbstractDBOtpProvider implements OtpProvider {
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
    public OtpToken generateToken(OtpGenerationRequest request) {
        OtpProfile profile = otpProfileService.getOtpProfile(request.getDomainId(), request.getProfile())
                .orElseThrow(ProfileNotFoundException::new);
        OtpToken token = new OtpToken();
        token.setDomainId(request.getDomainId());
        token.setKey(request.getKey());
        token.setProfile(request.getProfile());
        token.setValue(generateToken(profile));
        token.setExpiryTime(ZonedDateTime.now().plus(profile.getOtpValidity().getValue(), profile.getOtpValidity().getTimeUnit()));
        token.setType(type());
        token.setMaxAttempts(profile.getMaxAttempts());
        return otpTokenRepository.save(token);
    }

    protected abstract String generateToken(OtpProfile profile);

    @Override
    public boolean validateToken(OtpValidationRequest request) {
        Optional<OtpToken> token = otpTokenRepository.findById(request.getOtpId());

        if (token.isEmpty()) {
            return false;
        }
        OtpToken tokenValue = token.get();
        boolean attemptsExceeded = tokenValue.recordAttempt();
        boolean valid = tokenValue.getDomainId().equals(request.getDomainId()) &&
                tokenValue.getType().equals(type()) &&
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
