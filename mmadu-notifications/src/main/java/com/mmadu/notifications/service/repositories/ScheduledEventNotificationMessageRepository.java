package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.ScheduledEventNotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduledEventNotificationMessageRepository extends MongoRepository<ScheduledEventNotificationMessage, String> {

    List<ScheduledEventNotificationMessage> findByDomainIdAndEventTriggersContains(@Param("domainId") String domainId,
                                                                                   @Param("event") String event);
}
