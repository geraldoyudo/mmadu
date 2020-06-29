package com.mmadu.service.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AppUserTest {

    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String TEST_ID = "test-id";
    private static final String DOMAIN_ID = "domain-id";
    private static final String PROPERTY_COUNTRY = "country";
    private static final String NIGERIA = "Nigeria";
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setPassword(PASSWORD);
        appUser.setUsername(USER);
        appUser.setId(TEST_ID);
        appUser.setDomainId(DOMAIN_ID);
        appUser.set(PROPERTY_COUNTRY, NIGERIA);
    }

    @Test
    void getPassword() {
        assertThat(appUser.getPassword(), equalTo("password"));
    }

    @Test
    void getUsername() {
        assertThat(appUser.getUsername(), equalTo(USER));
    }

    @Test
    void getID() {
        assertThat(appUser.getId(), equalTo(TEST_ID));
    }

    @Test
    void getDomainId() {
        assertThat(appUser.getDomainId(), equalTo(DOMAIN_ID));
    }

    @Test
    void getCountryCustomProperty() {
        assertThat(appUser.get(PROPERTY_COUNTRY).get(), equalTo(NIGERIA));
    }
}
