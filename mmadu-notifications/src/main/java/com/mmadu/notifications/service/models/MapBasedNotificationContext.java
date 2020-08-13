package com.mmadu.notifications.service.models;

import com.mmadu.notifications.endpoint.models.NotificationContext;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode
@ToString
public class MapBasedNotificationContext implements NotificationContext {
    private Map<String, Object> context;
    private NotificationUser user;

    public MapBasedNotificationContext() {
        this.context = new HashMap<>();
    }

    public MapBasedNotificationContext(Map<String, Object> context, NotificationUser user) {
        this.context = context;
        this.user = user;
    }

    public MapBasedNotificationContext(NotificationUser user) {
        this.user = user;
        this.context = new HashMap<>();
    }

    public MapBasedNotificationContext(Map<String, Object> context) {
        this.context = new HashMap<>(context);
    }

    @Override
    public Optional<NotificationUser> getUser() {
        return Optional.ofNullable(user);
    }

    @Override
    public <T> Optional<T> get(T key) {
        return Optional.ofNullable(context.get(key))
                .map(v -> (T) v);
    }

    @Override
    public Map<String, Object> getAsMap() {
        return new HashMap<>(context);
    }
}
