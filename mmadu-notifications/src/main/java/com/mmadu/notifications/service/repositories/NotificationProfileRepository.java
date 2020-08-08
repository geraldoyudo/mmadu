package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.NotificationProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationProfileRepository extends MongoRepository<NotificationProfile, String> {

    Optional<NotificationProfile> findByDomainIdAndProfileId(@Param("domainId") String domainId, @Param("profileId") String profileId);
}
