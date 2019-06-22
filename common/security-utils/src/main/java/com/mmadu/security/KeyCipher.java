package com.mmadu.security;

public interface KeyCipher {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
