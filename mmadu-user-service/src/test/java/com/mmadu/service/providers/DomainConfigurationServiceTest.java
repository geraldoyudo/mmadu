package com.mmadu.service.providers;

import static com.mmadu.service.providers.DomainConfigurationService.GLOBAL_DOMAIN_CONFIG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.exceptions.DomainNotFoundException;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.DomainConfigurationRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DomainConfigurationServiceTest {

    private DomainConfigurationService domainConfigurationService;
    @Mock
    private DomainConfigurationRepository domainConfigurationRepository;
    @Mock
    private AppDomainRepository appDomainRepository;

    private DomainConfiguration globalConfiguration;
    private DomainConfiguration domain1Configuration;
    private AppDomain domain1;
    private AppDomain domain2;

    @Captor
    private final ArgumentCaptor<DomainConfiguration> appDomainConfigurationCaptor = ArgumentCaptor.forClass(DomainConfiguration.class);

    @Before
    public void setUp() {
        domain1 = createDomain("1", "domain-1");
        domain2 = createDomain("2", "domain-2");
        doReturn(true).when(appDomainRepository).existsById("1");
        doReturn(true).when(appDomainRepository).existsById("2");
        doReturn(false).when(appDomainRepository).existsById("3");
        globalConfiguration = createConfig("global", "0", "1234");
        domain1Configuration = createConfig("domain-1", "domain-1", "4321");
        doReturn(Optional.of(globalConfiguration)).when(domainConfigurationRepository).findByDomainId("0");
        doReturn(Optional.of(domain1Configuration)).when(domainConfigurationRepository).findByDomainId("1");
        doReturn(true).when(domainConfigurationRepository).existsByDomainId("1");
        doReturn(false).when(domainConfigurationRepository).existsByDomainId("2");
        domainConfigurationService = new DomainConfigurationService();
        domainConfigurationService.setAppDomainRepository(appDomainRepository);
        domainConfigurationService.setDomainConfigurationRepository(domainConfigurationRepository);
    }

    private DomainConfiguration createConfig(String id, String domainId, String token) {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setAuthenticationApiToken(token);
        configuration.setDomainId(domainId);
        configuration.setId(id);
        return configuration;
    }

    private AppDomain createDomain(String id, String name) {
        AppDomain domain = new AppDomain();
        domain.setId(id);
        domain.setName(name);
        return domain;
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

    @Test(expected = DomainNotFoundException.class)
    public void givenDomainNotPresentWhenGetDomainConfigurationShouldThrowException() {
        domainConfigurationService.getConfigurationForDomain("3");
    }

    @Test
    public void givenNoGlobalDomainExistsWhenInitShouldCreateGlobalConfig(){
        domainConfigurationService.init();

        verify(domainConfigurationRepository, times(1)).save(appDomainConfigurationCaptor.capture());
        DomainConfiguration domainConfiguration = appDomainConfigurationCaptor.getValue();
        assertThat(domainConfiguration.getId(), equalTo(GLOBAL_DOMAIN_CONFIG));
        assertThat(domainConfiguration.getDomainId(), isEmptyString());
        assertThat(domainConfiguration.getAuthenticationApiToken(), isEmptyString());
    }

    @Test
    public void givenGlobalDomainExistsWhenInitShouldDoNothing(){
        doReturn(true).when(domainConfigurationRepository).existsById(GLOBAL_DOMAIN_CONFIG);
        domainConfigurationService.init();
        verify(domainConfigurationRepository, times(0)).save(any(DomainConfiguration.class));
    }
}