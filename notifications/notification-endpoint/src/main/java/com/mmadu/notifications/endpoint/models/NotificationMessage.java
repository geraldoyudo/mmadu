package com.mmadu.notifications.endpoint.models;

public interface NotificationMessage {

    String getId();

    String getMessageTemplate();

    String getMessage();

    NotificationContext getContext();

    NotificationMessageHeaders getHeaders();

    String getType();
}
