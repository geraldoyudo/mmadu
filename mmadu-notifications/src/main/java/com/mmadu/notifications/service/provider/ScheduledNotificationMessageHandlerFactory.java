package com.mmadu.notifications.service.provider;

public interface ScheduledNotificationMessageHandlerFactory {

    ScheduledNotificationMessageHandler getHandlerForDomain(String domainId);
}
