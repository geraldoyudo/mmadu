package com.mmadu.identity.services.domain;

import com.mmadu.identity.entities.DomainIdentityConfiguration;

import java.util.Optional;

public interface DomainIdentityConfigurationService {

    Optional<DomainIdentityConfiguration> findByDomainId(String domainId);
}
