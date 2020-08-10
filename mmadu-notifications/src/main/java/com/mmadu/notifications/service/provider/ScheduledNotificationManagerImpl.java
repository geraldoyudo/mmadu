package com.mmadu.notifications.service.provider;

import com.mmadu.event.bus.providers.EventSubscriber;
import com.mmadu.event.bus.providers.EventSubscriberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ScheduledNotificationManagerImpl implements ScheduledNotificationManager {
    private static final String ALL_USER_EVENTS = "user.*";

    private final Map<String, EventSubscriber> domainEventSubscriberMap = new ConcurrentHashMap<>();
    private EventSubscriberFactory eventSubscriberFactory;


    @Autowired
    public void setEventSubscriberFactory(EventSubscriberFactory eventSubscriberFactory) {
        this.eventSubscriberFactory = eventSubscriberFactory;
    }

    @Override
    public EventSubscriber getSubscriberForDomain(String domainId) {
        return domainEventSubscriberMap.computeIfAbsent(domainId,
                (domain) -> eventSubscriberFactory.createSubscription(domain, ALL_USER_EVENTS));
    }

    @Override
    public boolean domainHasSubscriber(String domainId) {
        return domainEventSubscriberMap.containsKey(domainId);
    }
}
