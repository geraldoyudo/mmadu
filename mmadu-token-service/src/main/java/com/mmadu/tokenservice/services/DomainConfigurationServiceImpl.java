package com.mmadu.tokenservice.services;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.exceptions.DomainConfigurationNotFoundException;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DomainConfigurationServiceImpl implements DomainConfigurationService {

    public static final String GLOBAL_DOMAIN_CONFIG = "0";
    private DomainConfigurationRepository domainConfigurationRepository;

    @Autowired
    public void setDomainConfigurationRepository(DomainConfigurationRepository domainConfigurationRepository) {
        this.domainConfigurationRepository = domainConfigurationRepository;
    }


    @Override
    public DomainConfiguration getConfigurationForDomain(String domainId) {

        if (domainConfigurationRepository.existsByDomainId(domainId)) {
            return domainConfigurationRepository.findByDomainId(domainId).get();
        } else {
            return domainConfigurationRepository.findByDomainId(GLOBAL_DOMAIN_CONFIG)
                    .orElseThrow(() -> new DomainConfigurationNotFoundException());
        }
    }

    @PostConstruct
    public void init() {
        if (!domainConfigurationRepository.existsById(GLOBAL_DOMAIN_CONFIG)) {
            DomainConfiguration configuration = new DomainConfiguration();
            configuration.setDomainId("");
            configuration.setId(GLOBAL_DOMAIN_CONFIG);
            configuration.setAuthenticationApiToken("");
            domainConfigurationRepository.save(configuration);
        }
    }
}
