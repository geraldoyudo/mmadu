package com.mmadu.notifications.endpoint.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as = NotificationMessage.class)
public interface NotificationMessage {

    String getId();

    String getMessageTemplate();

    String getMessage();

    NotificationContext getContext();

    NotificationMessageHeaders getHeaders();

    String getType();
}
