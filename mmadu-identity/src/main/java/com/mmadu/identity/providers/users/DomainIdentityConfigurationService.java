package com.mmadu.identity.providers.users;

import com.mmadu.identity.entities.DomainIdentityConfiguration;

import java.util.Optional;

public interface DomainIdentityConfigurationService {

    Optional<DomainIdentityConfiguration> findByDomainId(String domainId);
}
