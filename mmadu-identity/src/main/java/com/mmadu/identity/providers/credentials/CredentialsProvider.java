package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.models.security.CredentialGenerationRequest;

public interface CredentialsProvider {
    boolean apply(CredentialGenerationRequest request);

    CredentialData generateCredential(CredentialGenerationRequest request);
}
