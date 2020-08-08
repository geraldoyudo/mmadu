package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.ProviderConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationProviderConfigurationRepository extends MongoRepository<ProviderConfiguration, String> {

    Optional<ProviderConfiguration> findByDomainIdAndProfileIdAndProviderId(
            @Param("domainId") String domainId,
            @Param("profileId") String profileId,
            @Param("providerId") String providerId);
}
