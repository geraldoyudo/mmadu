package com.mmadu.service.repositories;

import com.mmadu.service.entities.DomainConfiguration;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainConfigurationRepository extends MongoRepository<DomainConfiguration, String> {
    Optional<DomainConfiguration> findByDomainId(String domainId);
    boolean existsByDomainId(String domainId);
}
