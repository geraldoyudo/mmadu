package com.mmadu.notifications.defaultrestsms.providers;

import com.mmadu.notifications.defaultrestsms.exceptions.RestProviderException;
import com.mmadu.notifications.defaultrestsms.models.DefaultRestSMSProperties;
import com.mmadu.notifications.defaultrestsms.models.RestSMSConfigurationProperties;
import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultRestSMSProvider implements NotificationProvider {
    private WebClientResolver resolver;
    private DefaultRestSMSProperties defaultProperties;
    private SMSTemplateEngine SMSTemplateEngine;

    @Override
    public Mono<Void> process(NotificationMessage message, NotificationProviderConfiguration configuration) {
        return Mono.just(message)
                .flatMap(m -> SMSTemplateEngine.evaluateTemplate(m.getMessageTemplate(), m.getContext()))
                .flatMap(processedMessage -> sendMessage(processedMessage, message, configuration));
    }

    private Mono<Void> sendMessage(String processedMessage, NotificationMessage message, NotificationProviderConfiguration configuration) {
        return Mono.defer(() -> resolver.resolveClient(configuration))
                .flatMap(webClient -> sendMessageWithClient(webClient, processedMessage, getDestination(message), configuration));
    }

    private Mono<Void> sendMessageWithClient(WebClient webClient, String processedMessage, List<String> destination, NotificationProviderConfiguration configuration) {
        RestSMSConfigurationProperties properties = new RestSMSConfigurationProperties(configuration, defaultProperties);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("username", properties.getUsername())
                        .queryParam("password", properties.getPassword())
                        .queryParam("sender", properties.getSenderId())
                        .queryParam("message", processedMessage)
                        .queryParam("mobiles", String.join(",", destination))
                        .build()
                ).retrieve()
                .bodyToMono(String.class)
                .flatMap(this::ensureSuccess)
                .then();
    }

    private List<String> getDestination(NotificationMessage message) {
        List<String> destination = message.getHeaders().get("destination");
        if (destination == null || destination.isEmpty()) {
            NotificationUser user = message.getContext().getUser()
                    .orElseThrow(() -> new IllegalArgumentException("destination not set and no user present"));
            destination = Collections.singletonList(
                    (String) user.getProperty("phoneNumber").orElseThrow(
                            () -> new IllegalArgumentException("destination not set and email property not present in user")
                    )
            );
        }
        return destination.stream()
                .map(d -> d.replace("+", "").replace(" ", ""))
                .collect(Collectors.toList());
    }

    private Mono<String> ensureSuccess(String response) {
        if (!response.contains("OK")) {
            return Mono.error(new RestProviderException(response));
        } else {
            return Mono.just(response);
        }
    }

    @Override
    public boolean supportsMessage(NotificationMessage message) {
        return message.getType().equalsIgnoreCase("sms")
                && !StringUtils.isEmpty(message.getMessageTemplate())
                && ((message.getContext().getUser().isPresent() && message.getContext().getUser().get().getProperty("phoneNumber").isPresent()) ||
                !message.getHeaders().get("destination").isEmpty());
    }

    public void setDefaultProperties(DefaultRestSMSProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public void setResolver(WebClientResolver resolver) {
        this.resolver = resolver;
    }

    public void setTemplateEngine(SMSTemplateEngine SMSTemplateEngine) {
        this.SMSTemplateEngine = SMSTemplateEngine;
    }
}
