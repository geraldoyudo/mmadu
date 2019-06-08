package com.mmadu.service.utilities;

import com.mmadu.service.providers.PasswordHasher;

public class TestPasswordHasher implements PasswordHasher {
    public static final String HASH_PREFIX = "hash-";

    @Override
    public String hashPassword(String password) {
        return HASH_PREFIX + password;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword.replace(HASH_PREFIX, ""));
    }

    @Override
    public boolean isEncoded(String value) {
        return value.startsWith(HASH_PREFIX);
    }
}