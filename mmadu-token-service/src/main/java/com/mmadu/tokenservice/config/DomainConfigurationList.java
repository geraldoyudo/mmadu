package com.mmadu.tokenservice.config;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.models.DomainConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-config")
public class DomainConfigurationList {
    private List<DomainConfiguration> domains = new ArrayList<>();

    public List<DomainConfiguration> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainConfiguration> domains) {
        this.domains = domains;
    }
}
