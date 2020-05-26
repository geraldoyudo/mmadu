package com.mmadu.service.providers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = UniqueUserIdGeneratorImpl.class)
class UniqueUserIdGeneratorImplTest {
    public static final String DOMAIN_ID = "1234";
    @Autowired
    private UniqueUserIdGenerator uniqueUserIdGenerator;

    @Test
    void generateUniqueId() {
        assertThat(uniqueUserIdGenerator.generateUniqueId(DOMAIN_ID), notNullValue());
    }
}
