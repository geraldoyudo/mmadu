package com.mmadu.event.bus.providers.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mmadu.event.bus.events.user.UserEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestEvent implements UserEvent {
    private String id;
    private String domain;
    private String userId;
    private String key;

    public TestEvent() {
    }

    @Override
    public String getUserEventType() {
        return "test";
    }
}