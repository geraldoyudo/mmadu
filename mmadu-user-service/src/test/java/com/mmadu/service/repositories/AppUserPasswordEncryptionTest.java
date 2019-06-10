package com.mmadu.service.repositories;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mmadu.service.databaselisteners.AppUserSaveListener;
import com.mmadu.service.entities.AppUser;
import com.mmadu.service.repositories.AppUserPasswordEncryptionTest.TestConfig;
import com.mmadu.service.providers.PasswordHasher;
import com.mmadu.service.utilities.AppUserPasswordHashUpdater;
import com.mmadu.service.utilities.TestPasswordHasher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest
@RunWith(SpringRunner.class)
@Import({AppUserSaveListener.class, AppUserPasswordHashUpdater.class, TestConfig.class})
public class AppUserPasswordEncryptionTest {
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String DOMAIN = "domain";
    private static final String ID = "1234";
    private static final String HASHED_PASSWORD = TestPasswordHasher.HASH_PREFIX + PASSWORD;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser user;

    @Before
    public void setUp(){
        appUserRepository.deleteAll();
        user = new AppUser();
        user.setUsername(USER);
        user.setPassword(PASSWORD);
        user.setDomainId(DOMAIN);
        user.setId(ID);
    }

    @Test
    public void passwordShouldHashOnSave(){
        user = appUserRepository.save(user);
        assertThat(user.getPassword(), is(equalTo(HASHED_PASSWORD)));
    }

    @Test
    public void retrievedUserShouldHaveHashedPassword(){
        user = appUserRepository.save(user);
        assertThat(appUserRepository.findById(ID).get().getPassword(), is(equalTo(HASHED_PASSWORD)));
    }

    public static class TestConfig {
        @Bean
        public PasswordHasher passwordHasher(){
            return new TestPasswordHasher();
        }
    }
}
