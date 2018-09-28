package com.mmadu.service.service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.BouncyCastleAesCbcBytesEncryptor;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "mmadu.domain.encrypt-keys", havingValue = "true")
public class MasterKeyCBCKeyCipher implements KeyCipher {
    private MasterKeyResolver masterKeyResolver;
    private BouncyCastleAesCbcBytesEncryptor encryptor;

    @Autowired
    public void setMasterKeyResolver(MasterKeyResolver masterKeyResolver) {
        this.masterKeyResolver = masterKeyResolver;
    }

    @PostConstruct
    public void initialize(){
        String masterKey = masterKeyResolver.getMasterKey();
        encryptor = new BouncyCastleAesCbcBytesEncryptor(masterKey, masterKey);
    }

    @Override
    public String encrypt(String data) {
        byte [] bytes = Hex.decode(data);
        return new String(Hex.encode(encryptor.encrypt(bytes)));
    }

    @Override
    public String decrypt(String encryptedData) {
        byte [] bytes = Hex.decode(encryptedData);
        return new String(Hex.encode(encryptor.decrypt(bytes)));
    }
}
