package com.mmadu.identity.providers.keys;

import com.mmadu.identity.exceptions.CredentialFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyPair;

@Component
public class RSAKeyCipher implements KeyCipher {
    private KeyPair keyPair;

    @Autowired
    @Qualifier("credentials")
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public byte[] encrypt(byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            return cipher.doFinal(key);
        } catch (Exception ex) {
            throw new CredentialFormatException("Error encrypting credentials", ex);
        }
    }

    @Override
    public byte[] decrypt(byte[] encryptedKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            return cipher.doFinal(encryptedKey);
        } catch (Exception ex) {
            throw new CredentialFormatException("Error decrypting credentials", ex);
        }
    }
}
