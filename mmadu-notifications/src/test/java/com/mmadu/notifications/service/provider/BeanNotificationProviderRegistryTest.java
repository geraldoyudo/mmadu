package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        BeanNotificationProviderRegistry.class,
        BeanNotificationProviderRegistryTest.TestConfig.class
})
class BeanNotificationProviderRegistryTest {
    @Autowired
    private NotificationProviderRegistry registry;
    @MockBean
    private NotificationMessage message;

    @Test
    void getProvider() {

        assertAll(
                () -> assertTrue(registry.getProvider("provider1", message).isPresent()),
                () -> assertTrue(registry.getProvider("provider2", message).isPresent()),
                () -> assertTrue(registry.getProvider("provider3", message).isEmpty()),
                () -> assertTrue(registry.getProvider("provider4", message).isEmpty())
        );
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public NotificationProviderRegistration providerOne() {
            NotificationProvider p = mock(NotificationProvider.class);
            when(p.supportsMessage(any())).thenReturn(true);
            return NotificationProviderRegistration.builder()
                    .provider(p)
                    .id("provider1")
                    .build();
        }

        @Bean
        public NotificationProviderRegistration providerTwo() {
            NotificationProvider p = mock(NotificationProvider.class);
            when(p.supportsMessage(any())).thenReturn(false);
            return NotificationProviderRegistration.builder()
                    .provider(p)
                    .id("provider2")
                    .build();
        }

        @Bean
        public NotificationProviderRegistration secondProviderTwo() {
            NotificationProvider p = mock(NotificationProvider.class);
            when(p.supportsMessage(any())).thenReturn(true);
            return NotificationProviderRegistration.builder()
                    .provider(p)
                    .id("provider2")
                    .build();
        }

        @Bean
        public NotificationProviderRegistration providerFour() {
            NotificationProvider p = mock(NotificationProvider.class);
            when(p.supportsMessage(any())).thenReturn(false);
            return NotificationProviderRegistration.builder()
                    .provider(p)
                    .id("provider4")
                    .build();
        }
    }
}