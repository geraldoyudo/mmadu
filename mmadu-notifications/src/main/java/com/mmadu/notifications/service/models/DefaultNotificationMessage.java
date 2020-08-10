package com.mmadu.notifications.service.models;

import com.mmadu.notifications.endpoint.models.NotificationContext;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationMessageHeaders;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DefaultNotificationMessage implements NotificationMessage {
    private final String id;
    private final String type;
    private final String messageTemplate;
    private final String message;
    private final NotificationContext context;
    private final NotificationMessageHeaders headers;
}
