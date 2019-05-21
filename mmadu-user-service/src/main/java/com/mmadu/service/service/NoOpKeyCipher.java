package com.mmadu.service.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mmadu.domain.encrypt-keys", havingValue = "false", matchIfMissing = true)
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
