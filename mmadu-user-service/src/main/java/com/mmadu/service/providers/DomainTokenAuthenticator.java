package com.mmadu.service.providers;

public interface DomainTokenAuthenticator {
    void authenticateDomain(String domainId, String tokenValue);
}
