package com.mmadu.notifications.endpoint.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.Optional;

@JsonSerialize(as = NotificationContext.class)
public interface NotificationContext {

    Optional<NotificationUser> getUser();

    <T> Optional<T> get(T key);

    @JsonAnyGetter
    Map<String, Object> getAsMap();
}
