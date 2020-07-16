package com.mmadu.identity.providers.credentials;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptCredentialHasher implements CredentialDataHashMatcher, CredentialDataHashProvider {
    private static final String GUARD_PATTERN = "$#%s#$";
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String hashId;

    @Value("${mmadu.credentials.hash.id:1232423232}")
    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    @Override
    public boolean matches(String data, String hashedData) {
        return passwordEncoder.matches(data, hashedData.replace(getGuard(), ""));
    }

    @Override
    public String hash(String data) {
        String guard = getGuard();
        if (data.startsWith(guard) && data.endsWith(guard)) {
            return data;
        } else {
            return String.format("%s%s%s", guard,
                    passwordEncoder.encode(data),
                    guard
            );
        }
    }

    private String getGuard() {
        return String.format(GUARD_PATTERN, hashId);
    }
}
