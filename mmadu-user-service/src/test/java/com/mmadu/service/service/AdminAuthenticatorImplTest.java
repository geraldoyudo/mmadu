package com.mmadu.service.service;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import com.mmadu.service.utilities.DomainAuthenticationConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminAuthenticatorImplTest {

    private static final String TOKEN = new String(new byte[128]);

    @Mock
    private AppTokenService tokenService;

    @InjectMocks
    private AdminAuthenticator adminAuthenticator = new AdminAuthenticatorImpl();

    @Test
    public void givenMatchedTokenWhenIsTokenAdminReturnTrue() {
        doReturn(true).when(tokenService).tokenMatches(ADMIN_TOKEN_ID, TOKEN);

        assertThat(adminAuthenticator.isTokenAdmin(TOKEN), equalTo(true));
    }

    @Test
    public void givenUnMatchedTokenWhenIsTokenAdminReturnFalse() {
        doReturn(false).when(tokenService).tokenMatches(ADMIN_TOKEN_ID, TOKEN);

        assertThat(adminAuthenticator.isTokenAdmin(TOKEN), equalTo(false));
    }
}