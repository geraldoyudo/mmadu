package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.repositories.DomainIdentityConfigurationRepository;
import com.mmadu.security.providers.authorization.DomainJwkSetUriResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(100)
public class DefaultDomainJwkSetUriResolver implements DomainJwkSetUriResolver {
    private DomainIdentityConfigurationRepository domainIdentityConfigurationRepository;
    private String baseUrl;

    @Autowired
    public void setDomainIdentityConfigurationRepository(DomainIdentityConfigurationRepository domainIdentityConfigurationRepository) {
        this.domainIdentityConfigurationRepository = domainIdentityConfigurationRepository;
    }

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return domainIdentityConfigurationRepository.findByDomainId(domain)
                .map(this::deriveJwkSetUri);
    }

    @Value("${mmadu.identity.url:http://localhost:${server.port}}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String deriveJwkSetUri(DomainIdentityConfiguration domainIdentityConfiguration) {
        return String.format("%s/metadata/%s/jwks.json", baseUrl,
                domainIdentityConfiguration.getDomainId());
    }
}
