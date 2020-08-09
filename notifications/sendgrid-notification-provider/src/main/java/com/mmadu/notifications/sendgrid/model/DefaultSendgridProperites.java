package com.mmadu.notifications.sendgrid.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmadu.notifications.sendgrid")
public class DefaultSendgridProperites {
    private String apiKey;
    private String endpointUrl = "https://api.sendgrid.com/v3/mail/send";
    private String senderEmail;
}
