package com.mmadu.identity.providers.keys;

public interface KeyCipher {

    byte[] encrypt(byte[] key);

    byte[] decrypt(byte[] encryptedKey);
}
