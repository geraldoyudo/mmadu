package com.mmadu.identity.services.security;

import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.models.security.CredentialGenerationRequest;

import java.util.Optional;

public interface CredentialService {

    Optional<CredentialData> findById(String credentialId);

    String generateCredentialForDomain(String domainId, CredentialGenerationRequest request);

    String getCredentialVerificationKey(String domainId, String credentialId);
}
