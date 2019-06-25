package com.mmadu.service.utilities;

import com.mmadu.service.providers.PasswordHasher;
import com.mmadu.service.utilities.AppUserPasswordHashUpdaterTest.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.service.utilities.TestPasswordHasher.HASH_PREFIX;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppUserPasswordHashUpdater.class, TestConfig.class})
public class AppUserPasswordHashUpdaterTest {
    private String ORIGINAL_PASSWORD_HASH = HASH_PREFIX + "password";
    private String NEW_PASSWORD = "new_password";
    private String NEW_PASSWORD_HASH = HASH_PREFIX + "new_password";

    @Autowired
    private AppUserPasswordHashUpdater passwordHashUpdater;

    @Test
    public void returnSamePasswordIfNoChange() {
        assertThat(passwordHashUpdater.updatePasswordHash(ORIGINAL_PASSWORD_HASH),
                is(equalTo(ORIGINAL_PASSWORD_HASH)));
    }

    @Test
    public void hashNewPasswordIfPasswordChanges() {
        assertThat(passwordHashUpdater.updatePasswordHash(NEW_PASSWORD),
                is(equalTo(NEW_PASSWORD_HASH)));
    }

    public static class TestConfig {

        @Bean
        public PasswordHasher passwordHasher() {
            return new TestPasswordHasher();
        }
    }
}