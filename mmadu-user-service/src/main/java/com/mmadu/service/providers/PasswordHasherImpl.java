package com.mmadu.service.providers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mmadu.domain.encrypt-passwords", havingValue = "true")
public class PasswordHasherImpl implements PasswordHasher {
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean isEncoded(String value) {
        return value.length() == 60 && value.startsWith("$2a$10$");
    }
}
