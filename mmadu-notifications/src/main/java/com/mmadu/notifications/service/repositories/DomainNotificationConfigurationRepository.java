package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomainNotificationConfigurationRepository extends MongoRepository<DomainNotificationConfiguration, String> {
}
