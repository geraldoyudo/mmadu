package com.mmadu.identity.services.security;

import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.repositories.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CredentialServiceImpl implements CredentialService {
    private CredentialRepository credentialRepository;

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Optional<CredentialData> findById(String credentialId) {
        return credentialRepository.findById(credentialId)
                .map(Credential::getData);
    }
}
