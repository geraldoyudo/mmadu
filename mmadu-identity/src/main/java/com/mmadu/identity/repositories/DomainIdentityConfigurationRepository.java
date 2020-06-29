package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DomainIdentityConfigurationRepository extends MongoRepository<DomainIdentityConfiguration, String> {

    Optional<DomainIdentityConfiguration> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
