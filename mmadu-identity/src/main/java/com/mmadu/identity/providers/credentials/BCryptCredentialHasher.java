package com.mmadu.identity.providers.credentials;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptCredentialHasher implements CredentialDataHashMatcher, CredentialDataHashProvider {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public boolean matches(String data, String hashedData) {
        return passwordEncoder.matches(data, hashedData);
    }

    @Override
    public String hash(String data) {
        return passwordEncoder.encode(data);
    }
}
