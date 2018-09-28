package com.mmadu.service.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mmadu.domain.encrypt-passwords", havingValue = "false", matchIfMissing = true)
public class NoOpPasswordHasher implements PasswordHasher {

    @Override
    public String hashPassword(String password) {
        return password;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }

    @Override
    public boolean isEncoded(String value) {
        return true;
    }
}
