package com.mmadu.service.repositories;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.AppDomain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
@Import(DatabaseConfig.class)
public class AppDomainRepositoryTest {

    private static final String DOMAIN_ID = "domain-id";
    private static final String TEST_DOMAIN = "test";

    @Autowired
    private AppDomainRepository appDomainRepository;

    @Before
    public void setUp() {
        appDomainRepository.deleteById(DOMAIN_ID);
        AppDomain domain = new AppDomain();
        domain.setId(DOMAIN_ID);
        domain.setName(TEST_DOMAIN);
        appDomainRepository.save(domain);
    }

    @Test
    public void existByIdShouldReturnTrueIfContains() {
        assertThat(appDomainRepository.existsById(DOMAIN_ID), is(equalTo(true)));
    }

    @Test
    public void existByIdShouldReturnFalseIfNotContains() {
        assertThat(appDomainRepository.existsById("invalid-domain"), is(equalTo(false)));
    }

}
