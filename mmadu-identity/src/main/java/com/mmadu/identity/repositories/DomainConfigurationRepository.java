package com.mmadu.identity.repositories;

import com.mmadu.identity.entities.DomainConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainConfigurationRepository extends MongoRepository<DomainConfiguration, String> {
}
