package com.mmadu.event.bus.providers;

import com.mmadu.event.bus.events.Event;
import reactor.core.publisher.Flux;

public interface EventSubscriber {

    <T extends Event> Flux<T> receiveEventsFor(Class<T> clazz);
}
