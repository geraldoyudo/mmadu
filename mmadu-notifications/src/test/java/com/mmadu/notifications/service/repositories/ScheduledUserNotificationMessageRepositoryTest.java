package com.mmadu.notifications.service.repositories;

import com.mmadu.notifications.service.entities.ScheduledUserNotificationMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class ScheduledUserNotificationMessageRepositoryTest {
    @Autowired
    private ScheduledUserNotificationMessageRepository repository;

    @Test
    void findByDomainIdAndEventTriggersContains() {
        ScheduledUserNotificationMessage message = new ScheduledUserNotificationMessage();
        message.setId("1234");
        message.setDomainId("1");
        message.setEventTriggers(Collections.singletonList("user.test"));
        message.setType("email");
        message.setMessageTemplate("d-12345");
        repository.save(message);
        assertEquals(1, repository.findByDomainIdAndEventTriggersContains("1", "user.test").size());
    }
}