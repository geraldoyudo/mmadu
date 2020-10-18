package com.mmadu.notifications.service.models;

public class DomainInitializedEvent {

    private String domainId;

    public DomainInitializedEvent(String domainId) {
        this.domainId = domainId;
    }

    public String getDomainId() {
        return domainId;
    }
}
