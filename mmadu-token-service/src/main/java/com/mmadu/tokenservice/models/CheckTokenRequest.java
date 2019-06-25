package com.mmadu.tokenservice.models;

public class CheckTokenRequest {
    private String token;
    private String domainId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}
