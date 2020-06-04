package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.mmadu.identity.exceptions.CredentialCreationException;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class RSACredentialsProvider implements CredentialsProvider {
    @Override
    public boolean apply(CredentialGenerationRequest request) {
        return "rsa".equals(request.getType());
    }

    @Override
    public CredentialData generateCredential(CredentialGenerationRequest request) {
        try {
            RSAKey key = new RSAKeyGenerator(2048)
                    .keyID("123")
                    .generate();
            RSACredentialData data = new RSACredentialData();
            data.setKeyData(key.toJSONString());
            return data;
        } catch (JOSEException ex) {
            throw new CredentialCreationException("error creating credentials", ex);
        }
    }
}
