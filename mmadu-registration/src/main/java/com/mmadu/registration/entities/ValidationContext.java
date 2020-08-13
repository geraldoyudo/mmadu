package com.mmadu.registration.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Document
public class ValidationContext {
    @Id
    private String id;
    private String domainId;
    private String userId;
    private String key;
    private ZonedDateTime initiatedTimestamp;
    private ZonedDateTime completedTimestamp;
    private Map<String, Object> data = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ZonedDateTime getInitiatedTimestamp() {
        return initiatedTimestamp;
    }

    public void setInitiatedTimestamp(ZonedDateTime initiatedTimestamp) {
        this.initiatedTimestamp = initiatedTimestamp;
    }

    public ZonedDateTime getCompletedTimestamp() {
        return completedTimestamp;
    }

    public void setCompletedTimestamp(ZonedDateTime completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void addAllData(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.ofNullable(data.get(key))
                .map(clazz::cast);
    }

    public void set(String key, Object value) {
        this.data.put(key, value);
    }
}
