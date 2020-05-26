package com.mmadu.service.utilities;

import com.mmadu.service.providers.PasswordHasher;
import com.mmadu.service.utilities.AppUserPasswordHashUpdaterTest.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import static com.mmadu.service.utilities.TestPasswordHasher.HASH_PREFIX;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = {AppUserPasswordHashUpdater.class, TestConfig.class})
public class AppUserPasswordHashUpdaterTest {
    private String ORIGINAL_PASSWORD_HASH = HASH_PREFIX + "password";
    private String NEW_PASSWORD = "new_password";
    private String NEW_PASSWORD_HASH = HASH_PREFIX + "new_password";

    @Autowired
    private AppUserPasswordHashUpdater passwordHashUpdater;

    @Test
    void returnSamePasswordIfNoChange() {
        assertThat(passwordHashUpdater.updatePasswordHash(ORIGINAL_PASSWORD_HASH),
                is(equalTo(ORIGINAL_PASSWORD_HASH)));
    }

    @Test
    void hashNewPasswordIfPasswordChanges() {
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