package com.mmadu.security;

public class NoOpKeyCipher implements KeyCipher {

    @Override
    public String encrypt(String data) {
        return data;
    }

    @Override
    public String decrypt(String encryptedData) {
        return encryptedData;
    }
}
