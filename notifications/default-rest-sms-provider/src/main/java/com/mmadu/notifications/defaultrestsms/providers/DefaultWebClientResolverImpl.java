package com.mmadu.notifications.defaultrestsms.providers;

import com.mmadu.notifications.defaultrestsms.models.DefaultRestSMSProperties;
import com.mmadu.notifications.defaultrestsms.models.RestSMSConfigurationProperties;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class DefaultWebClientResolverImpl implements WebClientResolver {
    private Map<String, WebClient> webClientMap = new HashMap<>();
    private DefaultRestSMSProperties defaultProperties;

    @Override
    public Mono<WebClient> resolveClient(NotificationProviderConfiguration configuration) {
        RestSMSConfigurationProperties properties = new RestSMSConfigurationProperties(configuration, defaultProperties);
        return Mono.just(webClientMap.computeIfAbsent(properties.getEndpointUrl(),
                this::createWebClientForEndpoint));
    }

    public void setDefaultProperties(DefaultRestSMSProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    private WebClient createWebClientForEndpoint(String endpoint) {
        return WebClient.builder()
                .baseUrl(endpoint)
                .build();
    }
}
