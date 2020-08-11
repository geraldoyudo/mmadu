package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.service.models.GenericUserEvent;

public interface ScheduledNotificationMessageHandler {

    void handleEvent(GenericUserEvent event);
}
