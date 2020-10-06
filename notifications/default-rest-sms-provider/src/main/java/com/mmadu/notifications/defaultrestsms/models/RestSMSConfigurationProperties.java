package com.mmadu.notifications.defaultrestsms.models;

import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import org.springframework.util.StringUtils;

public class RestSMSConfigurationProperties {
    private NotificationProviderConfiguration configuration;
    private DefaultRestSMSProperties properties;

    public RestSMSConfigurationProperties(NotificationProviderConfiguration configuration, DefaultRestSMSProperties properties) {
        this.configuration = configuration;
        this.properties = properties;
    }

    public String getEndpointUrl() {
        return getProperty("rest_sms_endoint_url", properties.getEndpointUrl());
    }

    public String getSenderId() {
        return getProperty("rest_sms_sender_id", properties.getSenderId());
    }

    public String getUsername() {
        return getProperty("rest_sms_username", properties.getUsername());
    }

    public String getPassword() {
        return getProperty("rest_sms_password", properties.getPassword());
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
}
