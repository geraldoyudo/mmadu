package com.mmadu.tokenservice.models;

public class DomainConfig {
    private String id;
    private String name;
    private String authenticationApiToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthenticationApiToken() {
        return authenticationApiToken;
    }

    public void setAuthenticationApiToken(String authenticationApiToken) {
        this.authenticationApiToken = authenticationApiToken;
    }
}
