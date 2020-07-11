package com.mmadu.identity.services.domain;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.repositories.DomainIdentityConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DomainIdentityConfigurationServiceImpl implements DomainIdentityConfigurationService {
    private DomainIdentityConfigurationRepository domainIdentityConfigurationRepository;

    @Autowired
    public void setDomainIdentityConfigurationRepository(DomainIdentityConfigurationRepository domainIdentityConfigurationRepository) {
        this.domainIdentityConfigurationRepository = domainIdentityConfigurationRepository;
    }

    @Override
    @Cacheable("domainConfigurations")
    @Transactional(readOnly = true)
    public Optional<DomainIdentityConfiguration> findByDomainId(String domainId) {
        return domainIdentityConfigurationRepository.findByDomainId(domainId);
    }
}
