package com.mmadu.notifications.endpoint.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@EqualsAndHashCode
@ToString
public class NotificationUser {
    private String id;
    @JsonProperty("username")
    private String username;
    private Map<String, Object> properties = new LinkedHashMap<>();


    @JsonAnySetter
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    public Optional<Object> getProperty(String value) {
        return Optional.ofNullable(properties.get(value));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
