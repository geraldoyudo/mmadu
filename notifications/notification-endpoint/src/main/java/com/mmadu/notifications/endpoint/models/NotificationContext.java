package com.mmadu.notifications.endpoint.models;

import java.util.Map;
import java.util.Optional;

public interface NotificationContext {

    Optional<NotificationUser> getUser();

    <T> Optional<T> get(T key);

    Map<String, Object> getAsMap();
}
