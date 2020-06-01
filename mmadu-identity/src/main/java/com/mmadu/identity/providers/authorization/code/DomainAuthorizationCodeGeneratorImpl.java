package com.mmadu.identity.providers.authorization.code;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.providers.users.DomainIdentityConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DomainAuthorizationCodeGeneratorImpl implements DomainAuthorizationCodeGenerator {
    private Map<String, AuthorizationCodeGenerationProvider> providerMap = Collections.emptyMap();
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired(required = false)
    public void setProviders(List<AuthorizationCodeGenerationProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(AuthorizationCodeGenerationProvider::type, p -> p));
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    public String generateAuthorizationCodeAsDomain(String domainId) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(domainId)
                .orElseThrow(() -> new DomainNotFoundException("domain not found"));
        return Optional.ofNullable(providerMap.get(configuration.getGrantCodeType()))
                .orElseThrow(() -> new IllegalStateException("Grant code type not configured for domain"))
                .generateCode(configuration.getGrantCodeTypeProperties());
    }
}
