package com.mmadu.identity.services.metadata;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.credentials.CredentialData;
import com.mmadu.identity.exceptions.CredentialNotFoundException;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.providers.credentials.CredentialDecryptionProvider;
import com.mmadu.identity.providers.metadata.VerificationKeyProvider;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.services.security.CredentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class VerificationKeyServiceImpl implements VerificationKeyService {
    private List<VerificationKeyProvider> verificationKeyProviders = Collections.emptyList();
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private CredentialService credentialService;
    private CredentialDecryptionProvider credentialDecryptionProvider;

    @Autowired(required = false)
    public void setVerificationKeyProviders(List<VerificationKeyProvider> verificationKeyProviders) {
        this.verificationKeyProviders = verificationKeyProviders;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setCredentialDecryptionProvider(CredentialDecryptionProvider credentialDecryptionProvider) {
        this.credentialDecryptionProvider = credentialDecryptionProvider;
    }

    @Override
    @Cacheable("verificationKeys")
    public Map<String,Object> getKeys(String domainId) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
        Map<String, Object> properties = Optional.ofNullable(configuration.getAccessTokenProperties())
                .orElse(Collections.emptyMap());
        String credentialId = (String) Optional.ofNullable(properties.get("credentialId"))
                .orElseThrow(CredentialNotFoundException::new);
        try {
            CredentialData credentialData = credentialService.findById(credentialId)
                    .orElseThrow(CredentialNotFoundException::new);
            credentialData.decryptData(credentialDecryptionProvider);
            return getKeysFromCredentialData(credentialData);
        } catch (Exception ex) {
            log.error("could not fetch credentials", ex);
            throw new CredentialNotFoundException();
        }
    }

    private Map<String,Object> getKeysFromCredentialData(CredentialData data) {
        VerificationKeyProvider provider = verificationKeyProviders.stream()
                .filter(p -> p.supportsCredential(data))
                .findFirst()
                .orElseThrow(CredentialNotFoundException::new);
        return provider.getVerificationKeys(data);
    }
}
