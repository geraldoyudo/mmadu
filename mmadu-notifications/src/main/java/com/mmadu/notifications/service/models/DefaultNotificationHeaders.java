package com.mmadu.notifications.service.models;

import com.mmadu.notifications.endpoint.models.NotificationMessageHeaders;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
public class DefaultNotificationHeaders implements NotificationMessageHeaders {
    private final Map<String, List<String>> headers;

    public DefaultNotificationHeaders() {
        this.headers = new HashMap<>();
    }

    public DefaultNotificationHeaders(Map<String, Object> headers) {
        this.headers = new HashMap<>();
        headers.forEach((key, value) -> populateHeaderValue(this.headers, key, value));
    }

    private void populateHeaderValue(Map<String, List<String>> headers, String key, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof List) {
            headers.put(key, toStringList((List<?>) value));
        } else {
            headers.put(key, Collections.singletonList(value.toString()));
        }
    }

    private static List<String> toStringList(List<?> value) {
        return value.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> get(String key) {
        return headers.getOrDefault(key, Collections.emptyList());
    }
}
