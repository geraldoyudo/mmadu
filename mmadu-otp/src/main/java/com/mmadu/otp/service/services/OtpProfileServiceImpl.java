package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.repositories.OtpProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtpProfileServiceImpl implements OtpProfileService {
    private OtpProfileRepository otpProfileRepository;

    @Autowired
    public void setOtpProfileRepository(OtpProfileRepository otpProfileRepository) {
        this.otpProfileRepository = otpProfileRepository;
    }

    @Override
    public Optional<OtpProfile> getOtpProfile(String domainId, String identifier) {
        return otpProfileRepository.findByDomainIdAndIdentifier(domainId, identifier);
    }
}
