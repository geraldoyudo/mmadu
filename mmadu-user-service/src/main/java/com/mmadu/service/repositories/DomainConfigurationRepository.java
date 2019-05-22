package com.mmadu.service.repositories;

import com.mmadu.service.entities.DomainConfiguration;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface DomainConfigurationRepository extends MongoRepository<DomainConfiguration, String> {
    Optional<DomainConfiguration> findByDomainId(@Param("domainId") String domainId);
    boolean existsByDomainId(@Param("domainId") String domainId);
}
