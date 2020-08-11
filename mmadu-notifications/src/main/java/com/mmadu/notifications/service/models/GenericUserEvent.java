package com.mmadu.notifications.service.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.mmadu.event.bus.events.Event;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
public class GenericUserEvent implements Event {
    private String id;
    private String domain;
    private String userId;
    private String type;
    private Map<String, Object> properties;

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public <T> Optional<T> getProperty(String key) {
        return Optional.ofNullable(this.properties.get(key))
                .map(v -> (T) v);
    }
}
