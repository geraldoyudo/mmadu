package com.mmadu.identity.providers.metadata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.entities.credentials.RSACredentialData;
import com.mmadu.identity.exceptions.CredentialFormatException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

@Component
public class RSAVerificationKeyProvider implements VerificationKeyProvider {
    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsCredential(CredentialData credentialData) {
        return credentialData instanceof RSACredentialData;
    }

    @Override
    public Map<String, Object> getVerificationKeys(CredentialData credentialData) {
        try {
            RSACredentialData rsaData = (RSACredentialData) credentialData;
            RSAKey rsaKey = RSAKey.parse(rsaData.getKeyData());
            JWKSet jwkSet = new JWKSet(rsaKey.toPublicJWK());
            return objectMapper.convertValue(jwkSet.toJSONObject(), new TypeReference<>() {});
        } catch (ParseException ex) {
            throw new CredentialFormatException("could not parse credential", ex);
        }
    }
}
