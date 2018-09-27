package com.mmadu.service.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.exceptions.DomainAuthenticationException;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.exceptions.InvalidDomainCredentialsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AuthenticateApiAuthenticator.class)
public class AuthenticateApiAuthenticatorTest {
    public static final String VALID_DOMAIN = "1";
    public static final String VALID_TOKEN = "1234";
    public static final String VALID_GLOBAL_TOKEN = "1111";
    public static final String INVALID_TOKEN = "invalid-token";
    public static final String INVALID_DOMAIN = "invalid-domain";

    @Autowired
    private AuthenticateApiAuthenticator authenticateApiAuthenticator;

    @MockBean
    private DomainConfigurationService domainConfigurationService;
    @MockBean
    private AppTokenService appTokenService;

    private DomainConfiguration globalConfiguration;
    private DomainConfiguration domain1Configuration;

    @Before
    public void setUp(){
        globalConfiguration = new DomainConfiguration();
        globalConfiguration.setId("global");
        globalConfiguration.setDomainId("0");
        globalConfiguration.setTokenEncryptionKey(VALID_GLOBAL_TOKEN);
        domain1Configuration = new DomainConfiguration();
        domain1Configuration.setId("domain-1");
        domain1Configuration.setDomainId(VALID_DOMAIN);
        domain1Configuration.setTokenEncryptionKey(VALID_TOKEN);
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0) == invocationOnMock.getArgument(1))
                .when(appTokenService).tokenMatches(anyString(), anyString());
    }

    @Test
    public void givenValidDomainAndTokenConfiguredWhenAuthenticateDomainShouldBeSuccessful() {
        doReturn(domain1Configuration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, VALID_TOKEN);
    }

    @Test(expected = DomainAuthenticationException.class)
    public void givenValidDomainAndTokenConfiguredInDomainAuthenticateDomainWithGlobalTokenShouldThrowError() {
        doReturn(domain1Configuration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, VALID_GLOBAL_TOKEN);
    }

    @Test(expected = DomainAuthenticationException.class)
    public void givenValidDomainAndTokenConfiguredInDomainAuthenticateDomainWithInvalidTokenShouldThrowError() {
        doReturn(domain1Configuration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, INVALID_TOKEN);
    }

    @Test
    public void givenValidDomainAndTokenNotConfiguredInDomainAuthenticateDomainWithGlobalTokenShouldBeSuccessful() {
        doReturn(globalConfiguration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, VALID_GLOBAL_TOKEN);
    }

    @Test(expected = DomainAuthenticationException.class)
    public void givenValidDomainAndTokenNotConfiguredInDomainAuthenticateDomainWithInvalidTokenShouldThrowError() {
        doReturn(globalConfiguration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, INVALID_TOKEN);
    }

    @Test
    public void givenValidDomainAndNullTokenConfiguredInDomainAuthenticateDomainWithGlobalTokenShouldBeSuccessful() {
        domain1Configuration.setTokenEncryptionKey(null);
        doReturn(domain1Configuration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        doReturn(globalConfiguration).when(domainConfigurationService)
                .getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, VALID_GLOBAL_TOKEN);
    }

    @Test(expected = DomainAuthenticationException.class)
    public void givenValidDomainAndNullTokenConfiguredInDomainAuthenticateDomainWithInvalidTokenShouldThrowError() {
        domain1Configuration.setTokenEncryptionKey(null);
        doReturn(globalConfiguration).when(domainConfigurationService)
                .getConfigurationForDomain(DomainConfigurationService.GLOBAL_DOMAIN_CONFIG);
        doReturn(domain1Configuration).when(domainConfigurationService).getConfigurationForDomain(VALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, INVALID_TOKEN);
    }

    @Test(expected = DomainNotFoundException.class)
    public void givenInvalidDomainWithInvalidTokenShouldThrowError() {
        doThrow(new DomainNotFoundException())
                .when(domainConfigurationService).getConfigurationForDomain(INVALID_DOMAIN);
        authenticateApiAuthenticator.authenticateDomain(INVALID_DOMAIN, INVALID_TOKEN);
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenNullDomainAndNullTokenShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain(null, null);
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenEmptyDomainAndEmptyTokenShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain("", "");
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenEmptyDomainShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain("", VALID_TOKEN);
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenEmptyTokenShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, "");
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenNullTokenShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain(VALID_DOMAIN, null);
    }

    @Test(expected = InvalidDomainCredentialsException.class)
    public void givenNullDomainShouldThrowInvalidDomainCredentialsException() {
        authenticateApiAuthenticator.authenticateDomain("", VALID_TOKEN);
    }
}