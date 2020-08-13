package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.ScheduledUserNotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduledUserNotificationMessageRepository extends MongoRepository<ScheduledUserNotificationMessage, String> {

    List<ScheduledUserNotificationMessage> findByDomainIdAndEventTriggersContains(@Param("domainId") String domainId,
                                                                                  @Param("event") String event);
}
