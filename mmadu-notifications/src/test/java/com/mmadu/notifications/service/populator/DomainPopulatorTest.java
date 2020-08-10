package com.mmadu.notifications.service.populator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.notifications.service.repositories.DomainNotificationConfigurationRepository;
import com.mmadu.notifications.service.repositories.NotificationProfileRepository;
import com.mmadu.notifications.service.repositories.ProviderConfigurationRepository;
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
    private ObjectMapper objectMapper;
    @Autowired
    private DomainNotificationConfigurationRepository domainNotificationConfigurationRepository;
    @Autowired
    private NotificationProfileRepository notificationProfileRepository;
    @Autowired
    private ProviderConfigurationRepository providerConfigurationRepository;

    @Test
    public void checkPopulation() throws Exception {
        Thread.sleep(3000);
        assertAll(
                () -> assertEquals(1, domainNotificationConfigurationRepository.count()),
                () -> assertEquals(1, notificationProfileRepository.count()),
                () -> assertEquals(2, providerConfigurationRepository.count())
        );
    }
}