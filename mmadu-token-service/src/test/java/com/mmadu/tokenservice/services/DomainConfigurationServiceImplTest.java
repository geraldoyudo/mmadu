package com.mmadu.tokenservice.services;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.exceptions.DomainConfigurationNotFoundException;
import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.mmadu.tokenservice.services.DomainConfigurationServiceImpl.GLOBAL_DOMAIN_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DomainConfigurationServiceImplTest {

    private static final String TOKEN_ID = "1234";
    private static final String DOMAIN_ID = "1111";

    private DomainConfigurationServiceImpl domainConfigurationService;
    @Mock
    private DomainConfigurationRepository domainConfigurationRepository;
    @Mock
    private AppTokenRepository appTokenRepository;

    private DomainConfiguration globalConfiguration;
    private DomainConfiguration domain1Configuration;

    @Captor
    private final ArgumentCaptor<DomainConfiguration> appDomainConfigurationCaptor =
            ArgumentCaptor.forClass(DomainConfiguration.class);

    @Before
    public void setUp() {
        globalConfiguration = createConfig("global", "0", "1234");
        domain1Configuration = createConfig("domain-1", "domain-1", "4321");
        doReturn(Optional.of(globalConfiguration)).when(domainConfigurationRepository).findByDomainId("0");
        doReturn(Optional.of(domain1Configuration)).when(domainConfigurationRepository).findByDomainId("1");
        doReturn(true).when(domainConfigurationRepository).existsByDomainId("1");
        doReturn(false).when(domainConfigurationRepository).existsByDomainId("2");
        domainConfigurationService = new DomainConfigurationServiceImpl();
        domainConfigurationService.setDomainConfigurationRepository(domainConfigurationRepository);
        domainConfigurationService.setAppTokenRepository(appTokenRepository);
    }

    private DomainConfiguration createConfig(String id, String domainId, String token) {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setAuthenticationApiToken(token);
        configuration.setDomainId(domainId);
        configuration.setId(id);
        return configuration;
    }

    @Test
    public void whenConfigurationEntryIsPresentGetDomainConfigurationShouldReturnCorrectConfig() {
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain("1");
        assertThat(configuration.getId(), equalTo("domain-1"));
    }

    @Test
    public void whenConfigurationEntryIsNotPresentGetDomainConfigurationShouldReturnGlobalConfig() {
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain("2");
        assertThat(configuration.getId(), equalTo("global"));
    }

    @Ignore("functionality obsolete")
    @Test(expected = DomainConfigurationNotFoundException.class)
    public void givenDomainNotPresentWhenGetDomainConfigurationShouldThrowException() {
        domainConfigurationService.getConfigurationForDomain("3");
    }

    @Test
    public void givenNoGlobalDomainExistsWhenInitShouldCreateGlobalConfig() {
        domainConfigurationService.init();

        verify(domainConfigurationRepository, times(1))
                .save(appDomainConfigurationCaptor.capture());
        DomainConfiguration domainConfiguration = appDomainConfigurationCaptor.getValue();
        assertThat(domainConfiguration.getId(), equalTo(GLOBAL_DOMAIN_CONFIG));
        assertThat(domainConfiguration.getDomainId(), isEmptyString());
        assertThat(domainConfiguration.getAuthenticationApiToken(), isEmptyString());
    }

    @Test
    public void givenGlobalDomainExistsWhenInitShouldDoNothing() {
        doReturn(true).when(domainConfigurationRepository).existsById(GLOBAL_DOMAIN_CONFIG);
        domainConfigurationService.init();
        verify(domainConfigurationRepository, times(0)).save(any(DomainConfiguration.class));
    }

    @Test(expected = TokenNotFoundException.class)
    public void givenTokenIdNotExistsWhenSetDomainAuthTokenThrowTokenNotFoundException() {
        doReturn(false).when(appTokenRepository).existsById(TOKEN_ID);
        domainConfigurationService.setAuthTokenForDomain(TOKEN_ID, DOMAIN_ID);
    }

    @Test
    public void givenTokenIdAndDomainIdWhenSetDomainAuthTokenSaveNewDomainTokenConfiguration() {
        doReturn(true).when(appTokenRepository).existsById(TOKEN_ID);
        doReturn(new DomainConfiguration()).when(domainConfigurationRepository)
                .save(any(DomainConfiguration.class));
        domainConfigurationService.setAuthTokenForDomain(TOKEN_ID, DOMAIN_ID);
        verify(domainConfigurationRepository).save(appDomainConfigurationCaptor.capture());
        DomainConfiguration configuration = appDomainConfigurationCaptor.getValue();
        assertThat(configuration.getAuthenticationApiToken(), equalTo(TOKEN_ID));
        assertThat(configuration.getDomainId(), equalTo(DOMAIN_ID));
    }
}