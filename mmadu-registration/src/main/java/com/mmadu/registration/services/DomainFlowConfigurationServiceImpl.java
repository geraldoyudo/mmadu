package com.mmadu.registration.services;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.models.themes.ThemeConfiguration;
import com.mmadu.registration.repositories.DomainFlowConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DomainFlowConfigurationServiceImpl implements DomainFlowConfigurationService {
    private DomainFlowConfigurationRepository domainFlowConfigurationRepository;
    private ThemeConfiguration defaultThemeConfiguration;

    @Autowired
    public void setDefaultThemeConfiguration(ThemeConfiguration defaultThemeConfiguration) {
        this.defaultThemeConfiguration = defaultThemeConfiguration;
    }

    @Autowired
    public void setDomainFlowConfigurationRepository(DomainFlowConfigurationRepository domainFlowConfigurationRepository) {
        this.domainFlowConfigurationRepository = domainFlowConfigurationRepository;
    }

    @Override
    @Cacheable("domainFlowConfigurations")
    @Transactional(readOnly = true)
    public Optional<DomainFlowConfiguration> findByDomainId(String domainId) {
        return domainFlowConfigurationRepository.findByDomainId(domainId)
                .map(this::setDefaultThemeIfNull);
    }

    private DomainFlowConfiguration setDefaultThemeIfNull(DomainFlowConfiguration config) {
        if (config.getTheme() == null) {
            config.setTheme(defaultThemeConfiguration);
        }
        return config;
    }
}
