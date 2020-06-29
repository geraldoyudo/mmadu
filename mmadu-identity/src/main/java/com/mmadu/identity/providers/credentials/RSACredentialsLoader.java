package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.mmadu.identity.exceptions.CredentialFormatException;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.services.security.CredentialService;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Qualifier("rsa")
public class RSACredentialsLoader implements CredentialsLoader<RSAKey> {
    private CredentialService credentialService;
    private CredentialDecryptionProvider decryptionProvider;

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setDecryptionProvider(CredentialDecryptionProvider decryptionProvider) {
        this.decryptionProvider = decryptionProvider;
    }

    @Override
    @Cacheable("rsaKeys")
    public RSAKey loadCredentialById(String credentialId) throws CredentialFormatException {
        CredentialData data = credentialService.findById(credentialId)
                .orElseThrow(CredentialNotFoundException::new);
        data.decryptData(decryptionProvider);
        if (!(data instanceof RSACredentialData)) {
            throw new IllegalStateException("invalid credential");
        }
        try {
            return RSAKey.parse(((RSACredentialData) data).getKeyData());
        } catch (ParseException ex) {
            throw new CredentialFormatException("invalid credential format");
        }
    }
}
