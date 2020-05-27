package com.mmadu.service.providers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(classes = PasswordHasherImpl.class)
@TestPropertySource(properties = "mmadu.domain.encrypt-passwords=true")
class PasswordHasherImplTest {
    private static final String PASSWORD = "password";

    @Autowired
    private PasswordHasher passwordHasher;

    @Test
    void hashedValueShouldBeTheSameRegardlessOfNumberOfOperations() {
        String hashedPassword = passwordHasher.hashPassword(PASSWORD);
        assertThat(hashedPassword, notNullValue());
        assertThat(hashedPassword, not(equalTo(PASSWORD)));
        List<Executable> assertions = new LinkedList<>();
        assertions.add(() -> assertThat(passwordHasher.isEncoded(PASSWORD), is(false)));
        System.out.println("Hashed Values");
        for (int i = 0; i < 10; ++i) {
            String hash = passwordHasher.hashPassword(PASSWORD);
            System.out.println(hash);
            assertions.add(() -> assertThat(passwordHasher.matches(PASSWORD, hash),
                    is(equalTo(true))));
            assertions.add(() -> assertThat(passwordHasher.isEncoded(hash), is(true)));
        }
        assertAll(assertions);
    }

    @Test
    void shouldBeAbleToDetectEncodedPasswords() {
        assertThat(passwordHasher
                        .isEncoded("$2a$10$UixxZEWO2E3XXQ97.j/EhugzjntruK4N3JvbejnmyA2gNOsNUF1my"),
                is(true));
    }
}