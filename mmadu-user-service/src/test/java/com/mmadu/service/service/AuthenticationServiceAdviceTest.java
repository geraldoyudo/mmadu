package com.mmadu.service.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.mmadu.service.exceptions.DomainAuthenticationException;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.interceptors.AuthenticationServiceAdvice;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.models.AuthenticationStatus;
import com.mmadu.service.service.AuthenticationServiceAdviceTest.TestConfig;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        AuthenticationServiceAdvice.class, TestConfig.class
})
@EnableAspectJAutoProxy
public class AuthenticationServiceAdviceTest {
    private static final String DOMAIN_ID = "1";
    private static final String TOKEN_VALUE = "1234";

    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private AuthenticateApiAuthenticator apiAuthenticator;
    @MockBean
    private HttpServletRequest httpServletRequest;

    @Test
    public void allowIfDomainIsAuthenticated(){
        doNothing().when(apiAuthenticator).authenticateDomain(DOMAIN_ID, TOKEN_VALUE);
        doReturn(TOKEN_VALUE).when(httpServletRequest).getHeader("domain-auth-token");
        authenticationService.authenticate(getAuthRequest());
    }

    private AuthenticateRequest getAuthRequest() {
        return AuthenticateRequest.builder()
        .username("user").password("password").domain(DOMAIN_ID).build();
    }

    @Test(expected = DomainNotFoundException.class)
    public void throwDomainNotFoundExceptionIfDomainIdNotFound(){
        doThrow(new DomainNotFoundException()).when(apiAuthenticator).authenticateDomain(DOMAIN_ID, TOKEN_VALUE);
        doReturn(TOKEN_VALUE).when(httpServletRequest).getHeader("domain-auth-token");
        authenticationService.authenticate(getAuthRequest());
    }

    @Test(expected = DomainAuthenticationException.class)
    public void throwDomainAuthenticationExceptionIfAuthenticationFails(){
        doThrow(new DomainAuthenticationException()).when(apiAuthenticator).authenticateDomain(DOMAIN_ID, TOKEN_VALUE);
        doReturn(TOKEN_VALUE).when(httpServletRequest).getHeader("domain-auth-token");
        authenticationService.authenticate(getAuthRequest());
    }

    public static class TestConfig {
        @Bean
        public AuthenticationService authenticationService(){
            return authRequest -> AuthenticateResponse.builder().status(AuthenticationStatus.AUTHENTICATED).build();
        }
    }
}
