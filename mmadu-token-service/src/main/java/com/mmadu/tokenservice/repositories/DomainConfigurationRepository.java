package com.mmadu.tokenservice.repositories;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DomainConfigurationRepository extends MongoRepository<DomainConfiguration, String> {
    Optional<DomainConfiguration> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
