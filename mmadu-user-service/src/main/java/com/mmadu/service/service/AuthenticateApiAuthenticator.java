package com.mmadu.service.service;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.exceptions.DomainAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Qualifier("authenticateApi")
public class AuthenticateApiAuthenticator implements DomainTokenAuthenticator {

    private DomainConfigurationService domainConfigurationService;
    private AppTokenService appTokenService;

    @Autowired
    public void setDomainConfigurationService(DomainConfigurationService domainConfigurationService) {
        this.domainConfigurationService = domainConfigurationService;
    }

    @Autowired
    public void setAppTokenService(AppTokenService appTokenService) {
        this.appTokenService = appTokenService;
    }

    @Override
    public void authenticateDomain(String domainId, String tokenValue) {
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain(domainId);
        String token = configuration.getTokenEncryptionKey();
        if(StringUtils.isEmpty(token)){
            configuration = domainConfigurationService.getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
            token = configuration.getTokenEncryptionKey();
        }
        if(!appTokenService.tokenMatches(token, tokenValue)){
            throw new DomainAuthenticationException();
        }
    }
}
