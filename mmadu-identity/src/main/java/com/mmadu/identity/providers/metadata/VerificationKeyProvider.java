package com.mmadu.identity.providers.metadata;

import com.mmadu.identity.entities.credentials.CredentialData;

import java.util.Map;

public interface VerificationKeyProvider {
    boolean supportsCredential(CredentialData credentialData);

    Map<String, Object> getVerificationKeys(CredentialData credentialData);
}
