package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.service.models.GenericEvent;
import com.mmadu.notifications.service.models.GenericUserEvent;

public interface ScheduledNotificationMessageHandler {

    void handleUserEvent(GenericUserEvent event);

    void handleEvent(GenericEvent event);
}
