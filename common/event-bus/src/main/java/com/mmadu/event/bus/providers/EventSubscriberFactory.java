package com.mmadu.event.bus.providers;

public interface EventSubscriberFactory {

    EventSubscriber createSubscription(String domainId, String eventType);
}
