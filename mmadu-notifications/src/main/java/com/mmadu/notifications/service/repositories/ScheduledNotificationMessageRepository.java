package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.ScheduledNotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduledNotificationMessageRepository extends MongoRepository<ScheduledNotificationMessage, String> {

    List<ScheduledNotificationMessage> findByDomainIdAndEventTriggersContains(@Param("domainId") String domainId,
                                                                              @Param("event") String event);
}
