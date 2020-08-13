package com.mmadu.notifications.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class ScheduledEventNotificationMessage {
    @Id
    private String id;
    private String domainId;
    private String type;
    private List<String> eventTriggers = Collections.emptyList();
    private String eventFilter;
    private String messageTemplate;
    private String message;
    private String profile = "default";
    @NotEmpty
    private String destinationExpression;
    private Map<String, Object> context = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEventTriggers() {
        return eventTriggers;
    }

    public void setEventTriggers(List<String> eventTriggers) {
        this.eventTriggers = eventTriggers;
    }

    public String getEventFilter() {
        return eventFilter;
    }

    public void setEventFilter(String eventFilter) {
        this.eventFilter = eventFilter;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDestinationExpression() {
        return destinationExpression;
    }

    public void setDestinationExpression(String destinationExpression) {
        this.destinationExpression = destinationExpression;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }
}
