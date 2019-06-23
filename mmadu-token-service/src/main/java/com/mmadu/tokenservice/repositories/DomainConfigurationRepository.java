package com.mmadu.tokenservice.repositories;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface DomainConfigurationRepository extends MongoRepository<DomainConfiguration, String> {
    Optional<DomainConfiguration> findByDomainId(@Param("domainId") String domainId);

    boolean existsByDomainId(@Param("domainId") String domainId);
}
