package com.mmadu.identity.providers.credentials;

import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AESCredentialCipherTest {
    @Autowired
    private AESCredentialCipher cipher;

    @Test
    void testCipher() throws Exception {
        String data = IOUtils.readInputStreamToString(new ClassPathResource("keys/key-data.json").getInputStream());
        String encryptedData = cipher.encrypt(data);
        System.out.println(encryptedData);
        assertEquals(data, cipher.decrypt(encryptedData));
    }
}