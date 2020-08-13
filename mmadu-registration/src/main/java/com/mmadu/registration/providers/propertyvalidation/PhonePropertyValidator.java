package com.mmadu.registration.providers.propertyvalidation;

import org.springframework.stereotype.Component;

@Component
public class PhonePropertyValidator extends OTPEventBasedPropertyValidator {
    @Override
    public String type() {
        return "sms";
    }
}
