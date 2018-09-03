package com.mmadu.service.service;

import static com.mmadu.service.models.AuthenticationStatus.AUTHENTICATED;
import static com.mmadu.service.models.AuthenticationStatus.PASSWORD_INVALID;
import static com.mmadu.service.models.AuthenticationStatus.USERNAME_INVALID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.models.AuthenticateRequest;
import com.mmadu.service.models.AuthenticateResponse;
import com.mmadu.service.repositories.AppUserRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String DOMAIN_ID = "domain-id";

    @Mock
    private AppUserRepository appUserRepository;

    private AppUser user;
    private AuthenticationServiceImpl authenticationService;

    @Before
    public void setUp() {
        user = new AppUser();
        user.setDomainId(DOMAIN_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        doReturn(Optional.of(user)).when(appUserRepository).findByUsernameAndDomainId(USERNAME, DOMAIN_ID);
        authenticationService = new AuthenticationServiceImpl();
        authenticationService.setAppUserRepository(appUserRepository);
    }

    @Test
    public void givenCorrectUserNameAndPasswordWhenAuthenticateThenReturnAuthenticated() {
        AuthenticateResponse response = authenticationService.authenticate(
                AuthenticateRequest.builder().domain(DOMAIN_ID).password(PASSWORD).username(USERNAME).build());
        assertThat(response.getStatus(), equalTo(AUTHENTICATED));
    }

    @Test
    public void givenCorrectUserNameAndIncorrectPasswordWhenAuthenticateThenReturnInvalidPassword() {
        AuthenticateResponse response = authenticationService.authenticate(
                AuthenticateRequest.builder().domain(DOMAIN_ID).password("invalid-password").username(USERNAME)
                        .build());
        assertThat(response.getStatus(), equalTo(PASSWORD_INVALID));
    }

    @Test
    public void givenIncorrectUserNameWhenAuthenticateThenReturnInvalidUsername() {
        AuthenticateResponse response = authenticationService.authenticate(
                AuthenticateRequest.builder().domain(DOMAIN_ID).password("invalid-password")
                        .username("invalid-username").build());
        assertThat(response.getStatus(), equalTo(USERNAME_INVALID));
    }

}