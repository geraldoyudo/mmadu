package com.mmadu.tokenservice.services;

import com.mmadu.tokenservice.entities.DomainConfiguration;

public interface DomainConfigurationService {

    DomainConfiguration getConfigurationForDomain(String domainId);

    boolean tokenMatchesDomain(String token, String domainId);
}
