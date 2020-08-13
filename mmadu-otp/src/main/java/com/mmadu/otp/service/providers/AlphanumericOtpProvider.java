package com.mmadu.otp.service.providers;

import com.mmadu.otp.service.entities.OtpProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class AlphanumericOtpProvider extends AbstractDBOtpProvider {
    public static final String OTP_TYPE = "alphanumeric";

    @Override
    protected String generateToken(OtpProfile profile) {
        return RandomStringUtils.randomAlphanumeric(profile.getOtpLength());
    }

    @Override
    public String type() {
        return OTP_TYPE;
    }
}
