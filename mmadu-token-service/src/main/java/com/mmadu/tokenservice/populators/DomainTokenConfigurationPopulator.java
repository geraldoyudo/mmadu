package com.mmadu.tokenservice.populators;

import com.mmadu.tokenservice.config.DomainConfigurationList;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class DomainTokenConfigurationPopulator {
    @Autowired
    private DomainConfigurationList domainConfigurationList;
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @PostConstruct
    public void init(){
        if(domainConfigurationRepository.count() == 0){
            domainConfigurationRepository.saveAll(domainConfigurationList.getDomains());
        }
    }
}
