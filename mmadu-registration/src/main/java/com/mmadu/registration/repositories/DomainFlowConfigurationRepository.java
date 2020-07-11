package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DomainFlowConfigurationRepository extends MongoRepository<DomainFlowConfiguration, String> {

    Optional<DomainFlowConfiguration> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
