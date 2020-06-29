package com.mmadu.identity.providers.authorization.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class AlphaNumericCodeGenerator implements AuthorizationCodeGenerationProvider {
    public static final String TYPE = "alphanumeric";

    @Value("${mmadu.authorization.grant-code.default-length:10}")
    private int defaultLength;

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String generateCode(Map<String, Object> properties) {
        Map<String, Object> config = Optional.ofNullable(properties).orElse(Collections.emptyMap());
        int length = (Integer) Optional.ofNullable(config.get("length")).orElse(defaultLength);
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
