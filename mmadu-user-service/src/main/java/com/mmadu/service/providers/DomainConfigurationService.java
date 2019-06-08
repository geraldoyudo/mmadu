package com.mmadu.service.providers;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.exceptions.DomainConfigurationNotFoundException;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.DomainConfigurationRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainConfigurationService {

    public static final String GLOBAL_DOMAIN_CONFIG = "0";
    private AppDomainRepository appDomainRepository;
    private DomainConfigurationRepository domainConfigurationRepository;

    @Autowired
    public void setAppDomainRepository(AppDomainRepository appDomainRepository) {
        this.appDomainRepository = appDomainRepository;
    }

    @Autowired
    public void setDomainConfigurationRepository(DomainConfigurationRepository domainConfigurationRepository) {
        this.domainConfigurationRepository = domainConfigurationRepository;
    }

    public DomainConfiguration getConfigurationForDomain(String domainId){
        if(!appDomainRepository.existsById(domainId)){
            throw new DomainNotFoundException();
        }
        if(domainConfigurationRepository.existsByDomainId(domainId)){
            return domainConfigurationRepository.findByDomainId(domainId).get();
        }else{
            return domainConfigurationRepository.findByDomainId(GLOBAL_DOMAIN_CONFIG)
                    .orElseThrow(() -> new DomainConfigurationNotFoundException());
        }
    }

    @PostConstruct
    public void init(){
        if(!domainConfigurationRepository.existsById(GLOBAL_DOMAIN_CONFIG)){
            DomainConfiguration configuration = new DomainConfiguration();
            configuration.setDomainId("");
            configuration.setId(GLOBAL_DOMAIN_CONFIG);
            configuration.setAuthenticationApiToken("");
            domainConfigurationRepository.save(configuration);
        }
    }
}
