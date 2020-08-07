package com.mmadu.notifications.endpoint;

import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface NotificationProvider {
    NotificationProviderConfiguration NONE = new NotificationProviderConfiguration() {
        @Override
        public <T> Optional<T> getProperty(String key) {
            return Optional.empty();
        }
    };

    Mono<Void> process(NotificationMessage message,
                       NotificationProviderConfiguration configuration);

    default Mono<Void> process(NotificationMessage message) {
        return process(message, NONE);
    }

    boolean supportsMessage(NotificationMessage message);
}
