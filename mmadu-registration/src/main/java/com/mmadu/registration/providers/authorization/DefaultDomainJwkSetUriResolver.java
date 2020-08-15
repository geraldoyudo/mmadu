package com.mmadu.registration.providers.authorization;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.repositories.DomainFlowConfigurationRepository;
import com.mmadu.security.providers.authorization.DomainJwkSetUriResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(100)
public class DefaultDomainJwkSetUriResolver implements DomainJwkSetUriResolver {
    private DomainFlowConfigurationRepository domainFlowConfigurationRepository;

    @Autowired
    public void setDomainFlowConfigurationRepository(DomainFlowConfigurationRepository domainFlowConfigurationRepository) {
        this.domainFlowConfigurationRepository = domainFlowConfigurationRepository;
    }

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return domainFlowConfigurationRepository.findByDomainId(domain)
                .map(DomainFlowConfiguration::getJwkSetUri);
    }
}
