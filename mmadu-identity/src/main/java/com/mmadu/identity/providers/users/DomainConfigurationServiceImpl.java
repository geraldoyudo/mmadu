package com.mmadu.identity.providers.users;

import com.mmadu.identity.entities.DomainConfiguration;
import com.mmadu.identity.repositories.DomainConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomainConfigurationServiceImpl implements DomainConfigurationService {
    private DomainConfigurationRepository domainConfigurationRepository;

    @Autowired
    public void setDomainConfigurationRepository(DomainConfigurationRepository domainConfigurationRepository) {
        this.domainConfigurationRepository = domainConfigurationRepository;
    }

    @Override
    @Cacheable("domainConfigurations")
    public Optional<DomainConfiguration> findByDomainId(String domainId) {
        return domainConfigurationRepository.findByDomainId(domainId);
    }
}
