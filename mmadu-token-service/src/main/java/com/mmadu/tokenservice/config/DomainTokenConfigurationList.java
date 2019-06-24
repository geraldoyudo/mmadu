package com.mmadu.tokenservice.config;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-token-config")
public class DomainTokenConfigurationList {
    private List<DomainConfiguration> domainTokens = new ArrayList<>();

    public List<DomainConfiguration> getDomainTokens() {
        return domainTokens;
    }

    public void setDomainTokens(List<DomainConfiguration> domainTokens) {
        this.domainTokens = domainTokens;
    }
}
