package com.mmadu.service.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DomainConfiguration {
    @Id
    private String id;
    private String domainId;
    private String authenticationApiToken;

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

    public String getAuthenticationApiToken() {
        return authenticationApiToken;
    }

    public void setAuthenticationApiToken(String authenticationApiToken) {
        this.authenticationApiToken = authenticationApiToken;
    }
}