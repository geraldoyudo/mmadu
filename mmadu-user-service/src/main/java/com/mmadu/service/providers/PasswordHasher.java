package com.mmadu.service.providers;

public interface PasswordHasher {

    String hashPassword(String password);

    boolean matches(String rawPassword, String encodedPassword);

    boolean isEncoded(String value);
}
