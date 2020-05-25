package com.mmadu.tokenservice.repositories;

import com.mmadu.tokenservice.entities.DomainConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@DataMongoTest
public class DomainConfigurationRepositoryTest {

    private static final String DOMAIN_ID = "domain-id";
    @Autowired
    private DomainConfigurationRepository domainConfigurationRepository;

    @BeforeEach
    void setUp() {
        domainConfigurationRepository.deleteAll();
    }

    @Test
    void testInsertAndQueryByDomainId() {
        createDomain();
        DomainConfiguration retrieved = domainConfigurationRepository.findByDomainId(DOMAIN_ID).get();
        assertThat(retrieved.getDomainId(), equalTo(DOMAIN_ID));
    }

    private void createDomain() {
        DomainConfiguration configuration = new DomainConfiguration();
        configuration.setDomainId(DOMAIN_ID);
        configuration.setAuthenticationApiToken("1234");
        domainConfigurationRepository.save(configuration);
    }

    @Test
    void existsByDomainId() {
        createDomain();
        assertThat(domainConfigurationRepository.existsByDomainId(DOMAIN_ID), equalTo(true));
    }
}
