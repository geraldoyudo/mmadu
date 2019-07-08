package com.mmadu.service.providers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PasswordHasherImpl.class)
@TestPropertySource(properties = "mmadu.domain.encrypt-passwords=true")
public class PasswordHasherImplTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String PASSWORD = "password";

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    public void hashedValueShouldBeTheSameRegardlessOfNumberOfOperations() {
        String hashedPassword = passwordHasher.hashPassword(PASSWORD);
        assertThat(hashedPassword, notNullValue());
        assertThat(hashedPassword, not(equalTo(PASSWORD)));
        collector.checkThat(passwordHasher.isEncoded(PASSWORD), is(false));
        System.out.println("Hashed Values");
        for(int i=0; i< 10; ++i) {
            String hash = passwordHasher.hashPassword(PASSWORD);
            System.out.println(hash);
            collector.checkThat(passwordHasher.matches(PASSWORD, hash),
                    is(equalTo(true)));
            collector.checkThat(passwordHasher.isEncoded(hash), is(true));
        }
    }

    @Test
    public void shouldBeAbleToDetectEncodedPasswords(){
        collector.checkThat(passwordHasher
                        .isEncoded("$2a$10$UixxZEWO2E3XXQ97.j/EhugzjntruK4N3JvbejnmyA2gNOsNUF1my"),
                is(true));
    }
}