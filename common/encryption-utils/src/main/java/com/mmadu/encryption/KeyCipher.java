package com.mmadu.encryption;

public interface KeyCipher {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
