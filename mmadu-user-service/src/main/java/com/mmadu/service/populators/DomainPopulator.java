package com.mmadu.service.populators;

import com.mmadu.service.config.DomainConfigurationList;
import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.models.DomainConfig;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.DomainConfigurationRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DomainPopulator {
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;
    @Autowired
    private AppDomainRepository appDomainRepository;
    @Autowired
    private DomainConfigurationList domainConfigurationList;

    @PostConstruct
    public void setUpDomains(){
        domainConfigurationList.getDomains().forEach( domainConfig -> {
            AppDomain domain = loadAppDomain(domainConfig);
            loadAppDomainConfiguration(domainConfig, domain);
        });
    }

    private AppDomain loadAppDomain(DomainConfig domainConfig) {
        if(appDomainRepository.existsById(domainConfig.getId())){
            return appDomainRepository.findById(domainConfig.getId()).get();
        }
        AppDomain domain = new AppDomain();
        domain.setId(domainConfig.getId());
        domain.setName(domainConfig.getName());
        domain = appDomainRepository.save(domain);
        return domain;
    }

    private void loadAppDomainConfiguration(DomainConfig domainConfig, AppDomain domain) {
        if(domainConfigurationRepository.existsByDomainId(domainConfig.getId())){
            return;
        }
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(domain.getId());
        configuration.setAuthenticationApiToken(domainConfig.getAuthenticationApiToken());
        domainConfigurationRepository.save(configuration);
    }
}
