package com.mmadu.notifications.service.provider;

import com.mmadu.event.bus.providers.EventSubscriber;

public interface ScheduledUserNotificationManager {

    EventSubscriber getSubscriberForDomain(String domainId);

    boolean domainHasSubscriber(String domainId);
}
