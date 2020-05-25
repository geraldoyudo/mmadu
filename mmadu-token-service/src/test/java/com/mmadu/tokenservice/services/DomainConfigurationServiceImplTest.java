package com.mmadu.tokenservice.services;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import com.mmadu.tokenservice.exceptions.TokenNotFoundException;
import com.mmadu.tokenservice.repositories.AppTokenRepository;
import com.mmadu.tokenservice.repositories.DomainConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mmadu.tokenservice.services.DomainConfigurationServiceImpl.GLOBAL_DOMAIN_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        domainConfigurationService = new DomainConfigurationServiceImpl();
        domainConfigurationService.setDomainConfigurationRepository(domainConfigurationRepository);
        domainConfigurationService.setAppTokenRepository(appTokenRepository);
    }

    private void stubDomain1Query() {
        domain1Configuration = createConfig("domain-1", "domain-1", "4321");
        doReturn(Optional.of(domain1Configuration)).when(domainConfigurationRepository).findByDomainId("1");
        doReturn(true).when(domainConfigurationRepository).existsByDomainId("1");
    }

    private void stubDomain2Query() {
        stubGlobalConfig();
        doReturn(false).when(domainConfigurationRepository).existsByDomainId("2");
    }

    private void stubGlobalConfig() {
        globalConfiguration = createConfig("global", "0", "1234");
        doReturn(Optional.of(globalConfiguration)).when(domainConfigurationRepository).findByDomainId("0");
    }

    private DomainConfiguration createConfig(String id, String domainId, String token) {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setAuthenticationApiToken(token);
        configuration.setDomainId(domainId);
        configuration.setId(id);
        return configuration;
    }

    @Test
    void whenConfigurationEntryIsPresentGetDomainConfigurationShouldReturnCorrectConfig() {
        stubDomain1Query();
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain("1");
        assertThat(configuration.getId(), equalTo("domain-1"));
    }

    @Test
    void whenConfigurationEntryIsNotPresentGetDomainConfigurationShouldReturnGlobalConfig() {
        stubDomain2Query();
        DomainConfiguration configuration = domainConfigurationService.getConfigurationForDomain("2");
        assertThat(configuration.getId(), equalTo("global"));
    }


    @Test
    void givenNoGlobalDomainExistsWhenInitShouldCreateGlobalConfig() {
        domainConfigurationService.init();

        verify(domainConfigurationRepository, times(1))
                .save(appDomainConfigurationCaptor.capture());
        DomainConfiguration domainConfiguration = appDomainConfigurationCaptor.getValue();
        assertThat(domainConfiguration.getId(), equalTo(GLOBAL_DOMAIN_CONFIG));
        assertThat(domainConfiguration.getDomainId(), isEmptyString());
        assertThat(domainConfiguration.getAuthenticationApiToken(), isEmptyString());
    }

    @Test
    void givenGlobalDomainExistsWhenInitShouldDoNothing() {
        doReturn(true).when(domainConfigurationRepository).existsById(GLOBAL_DOMAIN_CONFIG);
        domainConfigurationService.init();
        verify(domainConfigurationRepository, times(0)).save(any(DomainConfiguration.class));
    }

    @Test
    void givenTokenIdNotExistsWhenSetDomainAuthTokenThrowTokenNotFoundException() {
        doReturn(false).when(appTokenRepository).existsById(TOKEN_ID);
        assertThrows(TokenNotFoundException.class,
                () -> domainConfigurationService.setAuthTokenForDomain(TOKEN_ID, DOMAIN_ID)
        );
    }

    @Test
    void givenTokenIdAndDomainIdWhenSetDomainAuthTokenSaveNewDomainTokenConfiguration() {
        doReturn(true).when(appTokenRepository).existsById(TOKEN_ID);
        doReturn(new DomainConfiguration()).when(domainConfigurationRepository)
                .save(any(DomainConfiguration.class));
        domainConfigurationService.setAuthTokenForDomain(TOKEN_ID, DOMAIN_ID);
        verify(domainConfigurationRepository).save(appDomainConfigurationCaptor.capture());
        DomainConfiguration configuration = appDomainConfigurationCaptor.getValue();
        assertAll(
                () -> assertThat(configuration.getAuthenticationApiToken(), equalTo(TOKEN_ID)),
                () -> assertThat(configuration.getDomainId(), equalTo(DOMAIN_ID))
        );
    }
}