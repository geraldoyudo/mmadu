package com.mmadu.notifications.endpoint.models;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface NotificationMessageHeaders {

    List<String> get(String key);

    default Optional<String> getOne(String key) {
        return Optional.ofNullable(get(key)).orElse(Collections.emptyList())
                .stream()
                .findFirst();
    }
}
