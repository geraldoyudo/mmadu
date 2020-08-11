package com.mmadu.event.bus.providers;

import com.mmadu.event.bus.events.Event;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface EventPublisher {

    <T extends Event> Mono<Void> publishEvent(Publisher<T> events);
}
