package com.mmadu.registration.services;

import com.mmadu.registration.entities.DomainFlowConfiguration;

import java.util.Optional;

public interface DomainFlowConfigurationService {

    Optional<DomainFlowConfiguration> findByDomainId(String domainId);
}
