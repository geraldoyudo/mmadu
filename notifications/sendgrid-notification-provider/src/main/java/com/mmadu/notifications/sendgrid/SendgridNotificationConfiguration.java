package com.mmadu.notifications.sendgrid;

import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.sendgrid.provider.SendgridNotificationProvider;
import org.springframework.context.annotation.Bean;

public class SendgridNotificationConfiguration {

    @Bean
    public NotificationProviderRegistration sendgridRegistration() {
        return NotificationProviderRegistration.builder()
                .id("sendgrid")
                .provider(new SendgridNotificationProvider())
                .build();
    }
}
