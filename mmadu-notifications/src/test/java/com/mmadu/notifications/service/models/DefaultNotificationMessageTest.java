package com.mmadu.notifications.service.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultNotificationMessageTest {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new Jdk8Module());

    @Test
    void testSerialization() throws Exception {
        DefaultNotificationMessage message = DefaultNotificationMessage.builder()
                .id("1234")
                .type("email")
                .headers(new DefaultNotificationHeaders(Map.of("key1", 2)))
                .context(new MapBasedNotificationContext(Map.of("key2", "val2"), user()))
                .messageTemplate("13232")
                .message("message")
                .build();
        assertEquals(
                mapper.readTree(DefaultNotificationMessageTest.class.getClassLoader().getResourceAsStream("models/default-notification-message.json")),
                mapper.valueToTree(message)
        );
    }

    private NotificationUser user() {
        NotificationUser user = new NotificationUser();
        user.setId("1111");
        user.setUsername("admin");
        user.setProperty("country", "Nigeria");
        return user;
    }

}