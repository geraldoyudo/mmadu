package com.mmadu.service.providers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UniqueUserIdGeneratorImpl.class)
public class UniqueUserIdGeneratorImplTest {
    public static final String DOMAIN_ID = "1234";
    @Autowired
    private UniqueUserIdGenerator uniqueUserIdGenerator;

    @Test
    public void generateUniqueId(){
        assertThat(uniqueUserIdGenerator.generateUniqueId(DOMAIN_ID), notNullValue());
    }
}
