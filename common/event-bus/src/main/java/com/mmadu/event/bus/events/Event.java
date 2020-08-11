package com.mmadu.event.bus.events;

public interface Event {

    String getId();

    String getDomain();

    String getType();
}
