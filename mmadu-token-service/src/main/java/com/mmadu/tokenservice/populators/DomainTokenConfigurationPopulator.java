package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.config.DomainTokenConfigurationList;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DomainTokenConfigurationPopulator {
    @Autowired
    private DomainTokenConfigurationList domainTokenConfigurationList;
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @PostConstruct
    public void init() {
        if (domainConfigurationRepository.count() <= 1) {
            domainConfigurationRepository.saveAll(domainTokenConfigurationList.getDomainTokens());
        }
    }
}
