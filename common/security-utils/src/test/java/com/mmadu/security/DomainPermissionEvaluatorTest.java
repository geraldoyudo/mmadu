package com.mmadu.security;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DomainPermissionEvaluatorTest {

    private static final String TOKEN = new String(new byte[128]);
    private static final String DOMAIN_ID = "domain-one";
    private static final String DOMAIN = "domain";

    @Mock
    private DomainTokenChecker domainTokenChecker;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private final DomainPermissionEvaluator domainPermissionEvaluator = new DomainPermissionEvaluator();

    @Test
    void givenAdminTokenAndAdminPermissionWhenCheckTokenThenReturnTrue() {
        doReturn(true).when(domainTokenChecker).checkIfTokenMatchesDomainToken(TOKEN, "admin");
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(true));
    }

    @Test
    void givenNonAdminTokenAndAdminPermissionCheckTokenReturnFalse() {
        doReturn(false).when(domainTokenChecker).checkIfTokenMatchesDomainToken(TOKEN, "admin");
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(false));
    }

    @Test
    void givenDomainTokenAndDomainPermissionWhenHasDomainPermissionReturnTrue() {
        doReturn(true).when(domainTokenChecker).checkIfTokenMatchesDomainToken(TOKEN, DOMAIN_ID);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(true));
    }

    @Test
    void givenNotDomainTokenAndDomainPermissionWhenHasDomainPermissionReturnFalse() {
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(false));
    }

    @Test
    void givenNullTokenCheckTokenThenReturnFalse() {
        doReturn(null).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(false));
    }

    @Test
    void givenNullTokenWhenHasDomainPermissionThenReturnFalse() {
        doReturn(null).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(false));
    }
}