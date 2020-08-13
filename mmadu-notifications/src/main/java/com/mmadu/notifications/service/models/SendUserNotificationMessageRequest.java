package com.mmadu.notifications.service.models;

import com.mmadu.security.api.DomainPayload;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class SendUserNotificationMessageRequest implements DomainPayload {
    private String id = UUID.randomUUID().toString();
    @NotEmpty
    private String domainId;
    @NotEmpty
    private String profileId = "default";
    @NotEmpty
    private String userId;
    @NotEmpty
    private String type;
    private String messageTemplate;
    private String messageContent;
    private Map<String, Object> headers = Collections.emptyMap();
    private Map<String, Object> context = Collections.emptyMap();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }
}
