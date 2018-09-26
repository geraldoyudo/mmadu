package com.mmadu.service.repositories;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmadu.service.entities.AppUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Before
    public void setUp(){
        appUserRepository.deleteAll();
    }

    @Test
    public void createAndRetrieveAppUser(){
        AppUser user = new AppUser();
        user.setUsername("user");
        user.setPassword("password");
        user = appUserRepository.save(user);
        assertThat(user.getId(), notNullValue());
        AppUser retrievedUser = appUserRepository.findById(user.getId()).get();
        assertThat(retrievedUser, notNullValue());
    }

    @Test
    public void findByUserNameAndDomain(){
        initializeAppUser();
        AppUser user = appUserRepository.findByUsernameAndDomainId("user", "test").get();
        assertThat(user, notNullValue());
    }

    private void initializeAppUser() {
        AppUser createdUser = new AppUser();
        createdUser.setUsername("user");
        createdUser.setPassword("password");
        createdUser.setDomainId("test");
        appUserRepository.save(createdUser);
    }
}