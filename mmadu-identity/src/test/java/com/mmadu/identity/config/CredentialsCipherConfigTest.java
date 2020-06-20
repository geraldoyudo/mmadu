package com.mmadu.identity.config;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;

@SpringBootTest(classes = CredentialsCipherConfig.class)
class CredentialsCipherConfigTest {
    @Autowired
    @Qualifier("credentials")
    private KeyPair keyPair;

    @Test
    void testLoadKeyPair() {
        System.out.println(Hex.encodeHexString(keyPair.getPublic().getEncoded()));
    }

}