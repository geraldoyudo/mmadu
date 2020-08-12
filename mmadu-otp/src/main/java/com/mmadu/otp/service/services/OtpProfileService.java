package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.OtpProfile;

import java.util.Optional;

public interface OtpProfileService {

    Optional<OtpProfile> getOtpProfile(String domainId, String identifier);
}
