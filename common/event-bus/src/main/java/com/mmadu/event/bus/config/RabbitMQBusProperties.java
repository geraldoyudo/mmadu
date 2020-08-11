package com.mmadu.event.bus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmadu.event.bus.rabbitmq")
public class RabbitMQBusProperties {
    private String uri = "amqp://localhost";
    private String busExchange = "mmadu-events";
    private int poolSize = 3;
}
