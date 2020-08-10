package com.mmadu.notifications.service.config;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class NoOpNotificationProviderConfiguration {

    @Bean
    public NotificationProviderRegistration noOpNotificationProviderRegistration() {
        return NotificationProviderRegistration.builder()
                .id("none")
                .provider(new NotificationProvider() {
                    @Override
                    public Mono<Void> process(NotificationMessage message, com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration configuration) {
                        return Mono.fromRunnable(() -> {
                            log.info("No Handler set. Using NoOp Handler {} {}", message, configuration);
                        }).then();
                    }

                    @Override
                    public boolean supportsMessage(NotificationMessage message) {
                        return true;
                    }
                })
                .build();
    }
}
