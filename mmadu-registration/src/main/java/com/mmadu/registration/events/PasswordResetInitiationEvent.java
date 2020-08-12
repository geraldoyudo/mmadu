package com.mmadu.registration.events;

import com.mmadu.event.bus.events.user.UserEvent;

public class PasswordResetInitiationEvent implements UserEvent {
    private String id;
    private String domain;
    private String userId;
    private String resetLink;

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

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserEventType() {
        return "password.reset.initiation";
    }

    public String getResetLink() {
        return resetLink;
    }

    public void setResetLink(String resetLink) {
        this.resetLink = resetLink;
    }
}
