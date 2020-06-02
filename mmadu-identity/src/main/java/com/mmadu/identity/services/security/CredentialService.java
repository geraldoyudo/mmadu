package com.mmadu.identity.services.security;

import com.mmadu.identity.entities.credentials.CredentialData;

import java.util.Optional;

public interface CredentialService {

    Optional<CredentialData> findById(String credentialId);
}
