package com.mmadu.otp.service.populators;

import com.mmadu.otp.service.repositories.DomainOtpConfigurationRepository;
import com.mmadu.otp.service.repositories.OtpProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("populated")
class DomainPopulatorTest {
    @Autowired
    private DomainOtpConfigurationRepository domainOtpConfigurationRepository;
    @Autowired
    private OtpProfileRepository otpProfileRepository;

    @Test
    void populationShouldBeSuccessful() throws Exception {
        Thread.sleep(1000);
        assertAll(
                () -> assertEquals(1, domainOtpConfigurationRepository.count()),
                () -> assertEquals(1, otpProfileRepository.count())
        );
    }
}