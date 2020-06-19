package com.mmadu.identity.services.security;

import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.entities.credentials.HasVerificationKey;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.providers.credentials.CredentialsProvider;
import com.mmadu.identity.repositories.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CredentialServiceImpl implements CredentialService {
    private CredentialRepository credentialRepository;
    private List<CredentialsProvider> providers = Collections.emptyList();

    @Autowired(required = false)
    public void setProviders(List<CredentialsProvider> providers) {
        this.providers = providers;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Optional<CredentialData> findById(String credentialId) {
        return credentialRepository.findById(credentialId)
                .map(Credential::getData);
    }

    @Override
    public String generateCredentialForDomain(String domainId, CredentialGenerationRequest request) {
        CredentialsProvider provider = providers.stream()
                .filter(p -> p.apply(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("request not supported"));
        Credential credential = new Credential();
        credential.setType(request.getType());
        credential.setDomainId(domainId);
        credential.setData(provider.generateCredential(request));
        credential = credentialRepository.save(credential);
        return credential.getId();
    }

    @Override
    public String getCredentialVerificationKey(String domainId, String credentialId) {
        return credentialRepository.findById(credentialId)
                .filter(credential -> domainId.equals(credential.getDomainId()))
                .filter(credential -> credential.getData() instanceof HasVerificationKey)
                .map(credential -> (HasVerificationKey) credential.getData())
                .map(HasVerificationKey::getVerificationKey)
                .map(key -> new String(Hex.encode(key)))
                .orElseThrow(CredentialNotFoundException::new);
    }
}
