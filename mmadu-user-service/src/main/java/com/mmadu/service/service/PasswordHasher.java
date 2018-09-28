package com.mmadu.service.service;

public interface PasswordHasher {

    String hashPassword(String password);
    boolean matches(String rawPassword, String encodedPassword);
    boolean isEncoded(String value);
}
