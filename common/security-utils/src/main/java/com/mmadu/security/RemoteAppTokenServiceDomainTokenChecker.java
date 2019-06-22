package com.mmadu.security;

public class RemoteAppTokenServiceDomainTokenChecker implements DomainTokenChecker{
    private String tokenServiceUrl;

    public void setTokenServiceUrl(String tokenServiceUrl) {
        this.tokenServiceUrl = tokenServiceUrl;
    }

    @Override
    public boolean checkIfTokenMatchesDomainToken(String token, String domainId) {
        return true;
    }
}
