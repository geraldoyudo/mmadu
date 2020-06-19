package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.exceptions.CredentialFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.security.KeyPair;

@Component
public class RSACredentialCipher implements CredentialDecryptionProvider, CredentialEncryptionProvider {
    private KeyPair keyPair;

    @Autowired
    @Qualifier("credentials")
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] cipherContentBytes = Base64Utils.decodeFromString(data);
            byte[] decryptedContent = cipher.doFinal(cipherContentBytes);
            return new String(decryptedContent);
        } catch (Exception ex) {
            throw new CredentialFormatException("Error decrypting credentials", ex);
        }
    }

    @Override
    public String encrypt(String data) {
        try {
            byte[] contentBytes = data.getBytes();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] cipherContent = cipher.doFinal(contentBytes);
            return Base64Utils.encodeToString(cipherContent);
        } catch (Exception ex) {
            throw new CredentialFormatException("Error encrypting credentials", ex);
        }
    }
}
