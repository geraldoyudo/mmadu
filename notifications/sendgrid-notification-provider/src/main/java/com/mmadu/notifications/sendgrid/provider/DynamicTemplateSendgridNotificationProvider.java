package com.mmadu.notifications.sendgrid.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.sendgrid.model.DefaultSendgridProperites;
import com.mmadu.notifications.sendgrid.model.SendgridProperties;
import com.mmadu.notifications.sendgrid.model.SendgridRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
public class DynamicTemplateSendgridNotificationProvider implements NotificationProvider {
    private DefaultSendgridProperites defaultSendgridProperites;
    private WebClient sendgridClient;

    public void setSendgridClient(WebClient sendgridClient) {
        this.sendgridClient = sendgridClient;
    }

    public void setDefaultSendgridProperites(DefaultSendgridProperites defaultSendgridProperites) {
        this.defaultSendgridProperites = defaultSendgridProperites;
    }

    @Override
    public Mono<Void> process(NotificationMessage message, NotificationProviderConfiguration configuration) {
        return processMessage(message, configuration);
    }

    private Mono<Void> processMessage(NotificationMessage message, NotificationProviderConfiguration configuration) {
        log.info("Sendgrid: processing {} {}", message, configuration);
        SendgridProperties properties = new SendgridProperties(configuration, defaultSendgridProperites);
        return sendgridClient.post()
                .uri(properties.getEndpointUrl())
                .header("Authorization", String.format("Bearer %s", properties.getApiKey()))
                .body(BodyInserters.fromValue(
                        SendgridRequest.builder()
                                .from(SendgridRequest.Address.builder().email(properties.getSenderEmail()).build())
                                .templateId(message.getMessageTemplate())
                                .personalization(
                                        createPersonalization(message)
                                ).build()
                ))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    private SendgridRequest.Personalization createPersonalization(NotificationMessage message) {
        SendgridRequest.Personalization.PersonalizationBuilder builder = SendgridRequest.Personalization.builder()
                .data(message.getContext());
        List<String> destination = message.getHeaders().get("destination");
        if (destination == null || destination.isEmpty()) {
            NotificationUser user = message.getContext().getUser()
                    .orElseThrow(() -> new IllegalArgumentException("destination not set and no user present"));
            destination = Collections.singletonList(
                    (String) user.getProperty("email").orElseThrow(
                            () -> new IllegalArgumentException("destination not set and email property not present in user")
                    )
            );
        }
        destination.forEach(d -> builder.to(SendgridRequest.Address.builder().email(d).build()));
        return builder.build();
    }

    @Override
    public boolean supportsMessage(NotificationMessage message) {
        return message.getType().equalsIgnoreCase("email")
                && !StringUtils.isEmpty(message.getMessageTemplate())
                && ((message.getContext().getUser().isPresent() && message.getContext().getUser().get().getProperty("email").isPresent()) ||
                !message.getHeaders().get("destination").isEmpty());
    }
}
