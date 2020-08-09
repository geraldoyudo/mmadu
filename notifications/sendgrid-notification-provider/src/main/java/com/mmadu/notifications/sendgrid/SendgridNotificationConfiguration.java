package com.mmadu.notifications.sendgrid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.sendgrid.model.DefaultSendgridProperites;
import com.mmadu.notifications.sendgrid.provider.DynamicTemplateSendgridNotificationProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@EnableConfigurationProperties(DefaultSendgridProperites.class)
public class SendgridNotificationConfiguration {
    @Bean
    public NotificationProviderRegistration sendgridRegistration(DefaultSendgridProperites properites,
                                                                 @Qualifier("sendgridNotification") WebClient sendgridClient) {
        DynamicTemplateSendgridNotificationProvider provider = new DynamicTemplateSendgridNotificationProvider();
        provider.setDefaultSendgridProperites(properites);
        provider.setSendgridClient(sendgridClient);
        return NotificationProviderRegistration.builder()
                .id("sendgrid")
                .provider(provider)
                .build();
    }

    @Bean
    @Qualifier("sendgridNotification")
    public WebClient sendgridNotificationClient() {
        ObjectMapper objectMapper = objectMapper();
        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientDefaultCodecsConfigurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));

                }).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new Jdk8Module());
    }
}
