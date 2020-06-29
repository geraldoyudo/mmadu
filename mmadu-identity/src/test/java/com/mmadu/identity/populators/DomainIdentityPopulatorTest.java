package com.mmadu.identity.populators;

import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import com.mmadu.identity.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("populated")
class DomainIdentityPopulatorTest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientInstanceRepository clientInstanceRepository;
    @Autowired
    private DomainIdentityConfigurationRepository domainIdentityConfigurationRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ScopeRepository scopeRepository;
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialDataHashMatcher dataHashMatcher;

    @Test
    void testConfigRead() throws Exception {
        Thread.sleep(2000);
        assertAll(
                () -> assertEquals(1, clientRepository.count()),
                () -> assertEquals(1, clientInstanceRepository.count()),
                () -> assertEquals(1, domainIdentityConfigurationRepository.count()),
                () -> assertEquals(1, resourceRepository.count()),
                () -> assertEquals(1, scopeRepository.count()),
                () -> assertClientInstanceHasCorrectSecret(clientInstanceRepository.findAll().get(0)),
                () -> assertCredentialIdCreated(domainIdentityConfigurationRepository.findAll().get(0))
        );
    }

    private void assertClientInstanceHasCorrectSecret(ClientInstance clientInstance) {
        ClientSecretCredentials credentials = (ClientSecretCredentials) clientInstance.getCredentials();
        assertTrue(dataHashMatcher.matches("1234567890", credentials.getSecret()));
    }

    private void assertCredentialIdCreated(DomainIdentityConfiguration configuration) {
        Map<String, Object> properties = configuration.getAccessTokenProperties();
        String credentialId = (String) properties.get("credentialId");
        Credential credential = credentialRepository.findById(credentialId).get();
        assertAll(
                () -> assertEquals("rsa", credential.getType()),
                () -> assertNotNull(((RSACredentialData) credential.getData()).getKeyData())
        );
    }
}