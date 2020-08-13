package com.mmadu.otp.service.providers;

import com.mmadu.otp.service.entities.OtpProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class NumericOtpProvider extends AbstractDBOtpProvider {
    public static final String OTP_TYPE = "numeric";

    @Override
    protected String generateToken(OtpProfile profile) {
        return RandomStringUtils.randomNumeric(profile.getOtpLength());
    }

    @Override
    public String type() {
        return OTP_TYPE;
    }
}
