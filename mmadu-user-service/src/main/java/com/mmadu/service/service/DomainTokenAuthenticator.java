package com.mmadu.service.service;

public interface DomainTokenAuthenticator {
    void authenticateDomain(String domainId, String tokenValue);
}
