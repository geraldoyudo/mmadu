package com.mmadu.service.service;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.exceptions.DomainAuthenticationException;
import com.mmadu.service.exceptions.InvalidDomainCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Qualifier("authenticateApi")
@ConditionalOnProperty(value = "mmadu.domain.authenticate-api-security-enabled", havingValue = "true")
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
        if(StringUtils.isEmpty(domainId) || StringUtils.isEmpty(tokenValue)){
            throw new InvalidDomainCredentialsException();
        }
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain(domainId);
        String token = configuration.getAuthenticationApiToken();
        if(StringUtils.isEmpty(token)){
            configuration = domainConfigurationService.getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
            token = configuration.getAuthenticationApiToken();
        }
        if(!appTokenService.tokenMatches(token, tokenValue)){
            throw new DomainAuthenticationException();
        }
    }
}
