package com.mmadu.notifications.sendgrid.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import com.mmadu.notifications.sendgrid.model.DefaultSendgridProperites;
import com.mmadu.notifications.sendgrid.model.SendgridProperties;
import com.mmadu.notifications.sendgrid.model.SendgridRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

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
                                        SendgridRequest.Personalization.builder()
                                                .to(SendgridRequest.Address.builder().email("gerald.oyudo@gmail.com").build())
                                                .data(message.getContext())
                                                .build()
                                ).build()
                ))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public boolean supportsMessage(NotificationMessage message) {
        return message.getType().equalsIgnoreCase("email")
                && !StringUtils.isEmpty(message.getMessageTemplate())
                && message.getContext().getUser().isPresent()
                && message.getContext().getUser().get().getProperty("email").isPresent();
    }
}
