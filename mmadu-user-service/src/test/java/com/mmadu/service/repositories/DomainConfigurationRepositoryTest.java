package com.mmadu.service.repositories;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmadu.service.entities.DomainConfiguration;
import com.sun.java.browser.plugin2.DOM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
public class DomainConfigurationRepositoryTest {

    private static final String DOMAIN_ID = "domain-id";
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @Before
    public void setUp(){
        domainConfigurationRepository.deleteAll();
    }

    @Test
    public void testInsertAndQueryByDomainId(){
        createDomain();
        DomainConfiguration retrieved = domainConfigurationRepository.findByDomainId(DOMAIN_ID).get();
        assertThat(retrieved.getDomainId(), equalTo(DOMAIN_ID));
    }

    private void createDomain() {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(DOMAIN_ID);
        configuration.setTokenEncryptionKey("1234");
        domainConfigurationRepository.save(configuration);
    }

    @Test
    public void existsByDomainId(){
        createDomain();
        assertThat(domainConfigurationRepository.existsByDomainId(DOMAIN_ID), equalTo(true));
    }
}
