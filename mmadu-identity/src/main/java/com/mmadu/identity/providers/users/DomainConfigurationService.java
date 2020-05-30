package com.mmadu.identity.providers.users;

import com.mmadu.identity.entities.DomainConfiguration;

import java.util.Optional;

public interface DomainConfigurationService {

    Optional<DomainConfiguration> findByDomainId(String domainId);
}
