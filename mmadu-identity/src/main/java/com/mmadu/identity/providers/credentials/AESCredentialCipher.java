package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.entities.keys.Key;
import com.mmadu.identity.exceptions.CredentialFormatException;
import com.mmadu.identity.providers.keys.KeyCipher;
import com.mmadu.identity.repositories.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Optional;

@Component
public class AESCredentialCipher implements CredentialDecryptionProvider, CredentialEncryptionProvider {
    private String keyId;
    private KeyRepository keyRepository;
    private KeyCipher keyCipher;
    private SecretKey secretKey;

    @Value("${mmadu.identity.credentials.key-id:credential-cipher}")
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @Autowired
    public void setKeyCipher(KeyCipher keyCipher) {
        this.keyCipher = keyCipher;
    }

    @Autowired
    public void setKeyRepository(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @EventListener(ContextRefreshedEvent.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void refreshKey() throws Exception {
        Optional<Key> key = keyRepository.findById(keyId);
        if (key.isEmpty()) {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            this.secretKey = generator.generateKey();
            Key newKey = new Key();
            newKey.setId(keyId);
            newKey.setValue(keyCipher.encrypt(secretKey.getEncoded()));
            keyRepository.save(newKey);
        } else {
            Key loadedKey = key.get();
            byte[] decryptedKey = keyCipher.decrypt(loadedKey.getValue());
            secretKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
        }
    }

    @Override
    public String decrypt(String data) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = aesCipher.doFinal(Base64Utils.decodeFromString(data));
            return new String(cipherText);
        } catch (Exception ex) {
            throw new CredentialFormatException("could not decrypt data", ex);
        }
    }

    @Override
    public String encrypt(String data) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteCipherText = aesCipher.doFinal(data.getBytes());
            return Base64Utils.encodeToString(byteCipherText);
        } catch (Exception ex) {
            throw new CredentialFormatException("could not decrypt data", ex);
        }
    }
}
