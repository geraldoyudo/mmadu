package com.mmadu.service.providers;

public interface KeyCipher {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
