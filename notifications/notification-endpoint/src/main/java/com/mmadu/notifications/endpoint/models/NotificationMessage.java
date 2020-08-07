package com.mmadu.notifications.endpoint.models;

import java.util.Optional;

public interface NotificationMessage {

    String getId();

    Optional<String> getMessageTemplate();

    Optional<String> getMessage();

    Optional<NotificationContext> getContext();
}
