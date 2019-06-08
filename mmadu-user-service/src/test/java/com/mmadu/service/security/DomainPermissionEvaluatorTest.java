package com.mmadu.service.security;

import static com.mmadu.service.utilities.DomainAuthenticationConstants.ADMIN_TOKEN_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.models.DomainIdObject;
import com.mmadu.service.repositories.AppUserRepository;
import com.mmadu.service.security.domainidextractors.AppUserIdDomainIdExtractor;
import com.mmadu.service.security.domainidextractors.TransparentDomainIdExtractor;
import com.mmadu.service.providers.AppTokenService;
import com.mmadu.service.providers.DomainConfigurationService;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

@RunWith(MockitoJUnitRunner.class)
public class DomainPermissionEvaluatorTest {

    private static final String TOKEN = new String(new byte[128]);
    private static final String DOMAIN_ID = "domain-one";
    private static final String USER_ID = "user-one";
    private static final String DOMAIN_TOKEN_ID = "domain-token-one";
    private static final String DOMAIN = "domain";
    private static final String USER = "user";

    @Mock
    private AppTokenService appTokenService;
    @Mock
    private Authentication authentication;
    @Mock
    private DomainConfigurationService domainConfigurationService;
    @Mock
    private DomainConfiguration domainConfiguration;
    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private final DomainPermissionEvaluator domainPermissionEvaluator = new DomainPermissionEvaluator();

    @Before
    public void setUp(){
        doReturn(domainConfiguration).when(domainConfigurationService).getConfigurationForDomain(DOMAIN_ID);
        doReturn(DOMAIN_TOKEN_ID).when(domainConfiguration).getAuthenticationApiToken();
        doReturn(Optional.of(new DomainIdObject(DOMAIN_ID))).when(appUserRepository).findDomainIdForUser(USER_ID);
        TransparentDomainIdExtractor transparentDomainIdExtractor = new TransparentDomainIdExtractor();
        AppUserIdDomainIdExtractor appUserIdDomainIdExtractor = new AppUserIdDomainIdExtractor();
        appUserIdDomainIdExtractor.setAppUserRepository(appUserRepository);
        domainPermissionEvaluator.setDomainIdExtractors(Arrays.asList(transparentDomainIdExtractor,
                appUserIdDomainIdExtractor));
    }

    @Test
    public void givenAdminTokenAndAdminPermissionWhenHasAdminPermissionReturnTrue() {
        doReturn(true).when(appTokenService).tokenMatches(ADMIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(true));
    }

    @Test
    public void givenNonAdminTokenAndAdminPermissionWhenHasAdminPermissionReturnFalse() {
        doReturn(false).when(appTokenService).tokenMatches(ADMIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(false));
    }

    @Test
    public void givenDomainTokenAndDomainPermissionWhenHasDomainPermissionReturnTrue() {
        doReturn(true).when(appTokenService).tokenMatches(DOMAIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(true));
    }

    @Test
    public void givenNotDomainTokenAndDomainPermissionWhenHasDomainPermissionReturnFalse() {
        doReturn(false).when(appTokenService).tokenMatches(DOMAIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(false));
    }

    @Test
    public void givenNullRequestWhenHasAdminPermissionThenReturnTrue() {
        doReturn(null).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, "admin"),
                equalTo(false));
    }

    @Test
    public void givenNullRequestWhenHasDomainPermissionThenReturnTrue() {
        doReturn(null).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, DOMAIN, DOMAIN_ID),
                equalTo(false));
    }

    @Test
    public void givenAdminAndAdminTokenWhenHasPermissionForUserThenReturnTrue() {
        doReturn(true).when(appTokenService).tokenMatches(ADMIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, USER, USER_ID),
                equalTo(true));
    }

    @Test
    public void givenDomainPermissionAndDomainTokenWhenHasPermissionForUserThenReturnTrue() {
        doReturn(true).when(appTokenService).tokenMatches(DOMAIN_TOKEN_ID, TOKEN);
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, USER, USER_ID),
                equalTo(true));
    }

    @Test
    public void givenNoDomainAndAdminPermissionAndDomainTokenWhenHasPermissionForUserThenReturnFalse() {
        doReturn(TOKEN).when(authentication).getPrincipal();
        assertThat(domainPermissionEvaluator.hasPermission(authentication, USER, USER_ID),
                equalTo(false));
    }
}