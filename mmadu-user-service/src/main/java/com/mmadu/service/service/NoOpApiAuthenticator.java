package com.mmadu.service.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Qualifier("authenticateApi")
@ConditionalOnProperty(value = "mmadu.domain.authenticate-api-security-enabled", havingValue = "false",
        matchIfMissing = true)
public class NoOpApiAuthenticator implements DomainTokenAuthenticator {


    @Override
    public void authenticateDomain(String domainId, String tokenValue) {
        // do nothing
    }
}
