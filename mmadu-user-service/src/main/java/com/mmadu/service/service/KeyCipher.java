package com.mmadu.service.service;

public interface KeyCipher {

    String encrypt(String data);

    String decrypt(String encryptedData);
}
