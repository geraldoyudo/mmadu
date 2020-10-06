package com.mmadu.notifications.defaultrestsms.providers;

import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public interface WebClientResolver {

    Mono<WebClient> resolveClient(NotificationProviderConfiguration configuration);
}
