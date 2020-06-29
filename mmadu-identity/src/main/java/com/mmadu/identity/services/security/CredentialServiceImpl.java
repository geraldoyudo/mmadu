package com.mmadu.identity.services.security;

import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.entities.credentials.HasVerificationKey;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.models.security.CredentialGenerationRequest;
import com.mmadu.identity.providers.credentials.CredentialDataHashProvider;
import com.mmadu.identity.providers.credentials.CredentialDecryptionProvider;
import com.mmadu.identity.providers.credentials.CredentialEncryptionProvider;
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
    private CredentialEncryptionProvider encryptionProvider;
    private CredentialDataHashProvider hashProvider;
    private CredentialDecryptionProvider decryptionProvider;

    @Autowired(required = false)
    public void setProviders(List<CredentialsProvider> providers) {
        this.providers = providers;
    }

    @Autowired
    public void setCredentialRepository(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Autowired
    public void setEncryptionProvider(CredentialEncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
    }

    @Autowired
    public void setDecryptionProvider(CredentialDecryptionProvider decryptionProvider) {
        this.decryptionProvider = decryptionProvider;
    }

    @Autowired
    public void setHashProvider(CredentialDataHashProvider hashProvider) {
        this.hashProvider = hashProvider;
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
        credential.setData(generateCredentialData(request, provider));
        credential = credentialRepository.save(credential);
        return credential.getId();
    }

    private CredentialData generateCredentialData(CredentialGenerationRequest request, CredentialsProvider provider) {
        CredentialData data = provider.generateCredential(request);
        data.encryptData(encryptionProvider);
        data.hashData(hashProvider);
        return data;
    }

    @Override
    public String getCredentialVerificationKey(String domainId, String credentialId) {
        return credentialRepository.findById(credentialId)
                .filter(credential -> domainId.equals(credential.getDomainId()))
                .filter(credential -> credential.getData() instanceof HasVerificationKey)
                .map(this::getVerificationKey)
                .map(key -> new String(Hex.encode(key)))
                .orElseThrow(CredentialNotFoundException::new);
    }

    private byte[] getVerificationKey(Credential credential) {
        CredentialData data = credential.getData();
        data.decryptData(decryptionProvider);
        return ((HasVerificationKey) data).verificationKey();
    }
}
