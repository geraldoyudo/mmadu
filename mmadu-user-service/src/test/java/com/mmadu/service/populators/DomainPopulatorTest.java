package com.mmadu.service.populators;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.hamcrest.CoreMatchers.is;


import com.mmadu.service.config.DomainConfigurationList;
import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.entities.DomainConfiguration;
import com.mmadu.service.models.DomainConfig;
import com.mmadu.service.repositories.AppDomainRepository;
import com.mmadu.service.repositories.DomainConfigurationRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
@Import({
        DomainPopulator.class,
        DatabaseConfig.class
})
public class DomainPopulatorTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String TOKEN_ENCRYPTION_KEY = "1234";
    private static final String DOMAIN_NAME = "domain-name";
    private static final String DOMAIN_ID = "domain-id";

    @MockBean
    private DomainConfigurationList domainConfigurationList;
    @Autowired
    private DomainPopulator domainPopulator;
    @Autowired
    private AppDomainRepository appDomainRepository;
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @Before
    public void setUp(){
        appDomainRepository.deleteAll();
        domainConfigurationRepository.deleteAll();
        doReturn(domainList()).when(domainConfigurationList).getDomains();
    }

    private List<DomainConfig> domainList(){
        DomainConfig config = new DomainConfig();
        config.setId(DOMAIN_ID);
        config.setName(DOMAIN_NAME);
        config.setAuthenticationApiToken(TOKEN_ENCRYPTION_KEY);
        return Arrays.asList(config);
    }

    @Test
    public void initializeDomainObjects(){
        domainPopulator.setUpDomains();
        collector.checkThat(domainConfigurationRepository.existsByDomainId(DOMAIN_ID),
                is(true));
        collector.checkThat(appDomainRepository.existsById(DOMAIN_ID),
                is(true));
        AppDomain domain = appDomainRepository.findById(DOMAIN_ID).get();
        collector.checkThat(domain.getName(), equalTo(DOMAIN_NAME));
        DomainConfiguration configuration = domainConfigurationRepository.findByDomainId(DOMAIN_ID).get();
        collector.checkThat(configuration.getAuthenticationApiToken(), equalTo(TOKEN_ENCRYPTION_KEY));
    }
}
