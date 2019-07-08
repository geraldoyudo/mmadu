package com.mmadu.service.populators;

import com.mmadu.service.config.DomainConfigurationList;
import com.mmadu.service.repositories.AppDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DomainPopulator {
    @Autowired
    private AppDomainRepository appDomainRepository;
    @Autowired
    private DomainConfigurationList domainConfigurationList;

    @PostConstruct
    public void setUpDomains() {
        if (appDomainRepository.count() == 0) {
            appDomainRepository.saveAll(domainConfigurationList.getDomains());
        }
    }
}
