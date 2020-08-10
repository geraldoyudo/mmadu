package com.mmadu.notifications.sendgrid.model;

import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SendgridProperties {
    private NotificationProviderConfiguration configuration;
    private DefaultSendgridProperites defaultProperties;

    public SendgridProperties(NotificationProviderConfiguration configuration, DefaultSendgridProperites defaultProperties) {
        this.configuration = configuration;
        this.defaultProperties = defaultProperties;
    }

    public String getApiKey() {
        return getProperty("sendgrid.api-key", defaultProperties.getApiKey());
    }

    private String getProperty(String property, String defaultValue) {
        try {
            String value = configuration.getProperty(property)
                    .map(p -> (String) p)
                    .orElse(defaultValue);
            if (StringUtils.isEmpty(value)) {
                throw new IllegalStateException("Default property not configured. " +
                        "You must configure this in your provider configuration ==> " + property);
            }
            return value;
        } catch (ClassCastException ex) {
            return defaultValue;
        }
    }

    public String getEndpointUrl() {
        return getProperty("sendgrid.endpoint-url", defaultProperties.getEndpointUrl());
    }

    public String getSenderEmail() {
        return getProperty("sendgrid.sender-email", defaultProperties.getSenderEmail());
    }
}
