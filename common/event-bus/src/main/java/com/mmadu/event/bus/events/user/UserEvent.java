package com.mmadu.event.bus.events.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mmadu.event.bus.events.Event;

public interface UserEvent extends Event {

    @Override
    default String getType() {
        return String.format("user.%s", getUserEventType());
    }

    @JsonIgnore
    String getUserEventType();

    String getUserId();
}
