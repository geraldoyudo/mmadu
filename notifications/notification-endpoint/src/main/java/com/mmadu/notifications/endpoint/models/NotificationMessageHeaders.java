package com.mmadu.notifications.endpoint.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonSerialize(as = NotificationMessageHeaders.class)
public interface NotificationMessageHeaders {

    List<String> get(String key);

    default Optional<String> getOne(String key) {
        return Optional.ofNullable(get(key)).orElse(Collections.emptyList())
                .stream()
                .findFirst();
    }

    @JsonAnyGetter
    Map<String, Object> getAsMap();
}
