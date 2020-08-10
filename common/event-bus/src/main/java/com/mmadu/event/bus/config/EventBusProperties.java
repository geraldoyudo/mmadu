package com.mmadu.event.bus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mmadu.event.bus")
public class EventBusProperties {
    private String provider = "rabbitmq";
}
