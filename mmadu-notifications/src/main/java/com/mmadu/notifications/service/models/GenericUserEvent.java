package com.mmadu.notifications.service.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.mmadu.event.bus.events.Event;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class GenericUserEvent implements Event {
    private String id;
    private String domain;
    private String userId;
    private String type;
    private Map<String, Object> properties = new LinkedHashMap<>();

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

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
