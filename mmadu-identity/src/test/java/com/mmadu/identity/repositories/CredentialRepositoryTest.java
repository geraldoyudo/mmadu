package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class CredentialRepositoryTest {
    @Autowired
    private CredentialRepository credentialRepository;

    @Test
    void testPersistRSAKey() throws Exception {
        RSAKey key = new RSAKeyGenerator(2048)
                .keyID("123")
                .generate();
        Credential credential = new Credential();
        RSACredentialData data = new RSACredentialData();
        data.setKeyData(key.toJSONString());
        credential.setData(data);
        credential = credentialRepository.save(credential);
        Credential read = credentialRepository.findById(credential.getId()).get();
        RSAKey readKey = RSAKey.parse(((RSACredentialData) read.getData()).getKeyData());
        assertEquals(key, readKey);
    }
}