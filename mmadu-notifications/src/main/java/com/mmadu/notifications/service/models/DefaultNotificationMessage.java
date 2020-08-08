package com.mmadu.notifications.service.models;

import com.mmadu.notifications.endpoint.models.NotificationContext;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationMessageHeaders;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DefaultNotificationMessage implements NotificationMessage {
    private final String id;
    private final String type;
    private final String messageTemplate;
    private final String message;
    private final NotificationContext context;
    private final NotificationMessageHeaders headers;
}
