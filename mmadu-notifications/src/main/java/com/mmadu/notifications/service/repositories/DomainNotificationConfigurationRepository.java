package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DomainNotificationConfigurationRepository extends MongoRepository<DomainNotificationConfiguration, String> {

    boolean existsByDomainId(@Param("domainId") String domainId);

    Optional<DomainNotificationConfiguration> findByDomainId(@Param("domainId") String domainId);
}
