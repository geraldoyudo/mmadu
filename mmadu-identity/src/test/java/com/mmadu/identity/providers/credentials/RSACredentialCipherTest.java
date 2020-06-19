package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.config.CredentialsCipherConfig;
import com.nimbusds.jose.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        CredentialsCipherConfig.class,
        RSACredentialCipher.class
})
class RSACredentialCipherTest {
    @Autowired
    private RSACredentialCipher cipher;

    @Test
    void testCipher() throws Exception {
        String data = IOUtils.readInputStreamToString(new ClassPathResource("keys/key-data.json").getInputStream());
        String encryptedData = cipher.encrypt(data);
        System.out.println(encryptedData);
        assertEquals(data, cipher.decrypt(encryptedData));
    }
}