package com.mmadu.notifications.service.provider;

import com.mmadu.event.bus.providers.EventSubscriber;
import com.mmadu.event.bus.providers.EventSubscriberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScheduledEventNotificationManagerImpl implements ScheduledEventNotificationManager {
    private static final String ALL_GENERIC_EVENTS = "event.#";

    private final Map<String, EventSubscriber> domainEventSubscriberMap = new ConcurrentHashMap<>();
    private EventSubscriberFactory eventSubscriberFactory;


    @Autowired
    public void setEventSubscriberFactory(EventSubscriberFactory eventSubscriberFactory) {
        this.eventSubscriberFactory = eventSubscriberFactory;
    }

    @Override
    public EventSubscriber getSubscriberForDomain(String domainId) {
        return domainEventSubscriberMap.computeIfAbsent(domainId,
                (domain) -> eventSubscriberFactory.createSubscription(domain, ALL_GENERIC_EVENTS));
    }

    @Override
    public boolean domainHasSubscriber(String domainId) {
        return domainEventSubscriberMap.containsKey(domainId);
    }
}
