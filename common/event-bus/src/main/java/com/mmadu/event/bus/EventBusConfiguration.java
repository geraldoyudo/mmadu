package com.mmadu.event.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.event.bus.config.EventBusProperties;
import com.mmadu.event.bus.config.RabbitMQBusProperties;
import com.mmadu.event.bus.providers.EventPublisher;
import com.mmadu.event.bus.providers.EventSubscriberFactory;
import com.mmadu.event.bus.providers.rabbitmq.RabbitMQUserEventPublisher;
import com.mmadu.event.bus.providers.rabbitmq.RabbitMQUserEventSubscriberFactory;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

@Configuration
@EnableConfigurationProperties({
        EventBusProperties.class,
        RabbitMQBusProperties.class
})
public class EventBusConfiguration {


    @Configuration
    @ConditionalOnProperty(name = "mmadu.event.bus.provider", havingValue = "rabbitmq", matchIfMissing = true)
    public static class RabbitMQConfiguration {

        @Bean
        @Qualifier("mmaduEventBus")
        public Sender eventBusSender(RabbitMQBusProperties properties) throws Exception {
            return RabbitFlux.createSender(new SenderOptions()
                    .connectionFactory(outgoingRabbitMQEventBusConnectionFactory(properties))
                    .resourceManagementScheduler(Schedulers.elastic())
            );
        }

        @Bean
        @Qualifier("mmaduEventBus")
        public Receiver eventBusReceiver(RabbitMQBusProperties properties) throws Exception {
            return RabbitFlux.createReceiver(new ReceiverOptions()
                    .connectionFactory(incomingRabbitMQEventBusConnectionFactory(properties))
                    .connectionSubscriptionScheduler(Schedulers.elastic()));
        }

        @Bean
        public ConnectionFactory outgoingRabbitMQEventBusConnectionFactory(RabbitMQBusProperties properties)
                throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(properties.getUri());
            return factory;
        }

        @Bean
        public ConnectionFactory incomingRabbitMQEventBusConnectionFactory(RabbitMQBusProperties properties)
                throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(properties.getUri());
            factory.useNio();
            return factory;
        }

        @Bean
        public EventPublisher eventBusPublisher(@Qualifier("mmaduEventBus") Sender sender,
                                                RabbitMQBusProperties properties) {
            RabbitMQUserEventPublisher publisher = new RabbitMQUserEventPublisher();
            publisher.setSender(sender);
            publisher.setExchange(properties.getBusExchange());
            publisher.setMapper(new ObjectMapper());
            return publisher;
        }

        @Bean
        public EventSubscriberFactory eventSubscriberFactory(@Qualifier("mmaduEventBus") Sender sender,
                                                             @Qualifier("mmaduEventBus") Receiver receiver,
                                                             RabbitMQBusProperties properties) {
            RabbitMQUserEventSubscriberFactory factory = new RabbitMQUserEventSubscriberFactory();
            factory.setExchange(properties.getBusExchange());
            factory.setMapper(new ObjectMapper());
            factory.setReceiver(receiver);
            factory.setSender(sender);
            return factory;
        }
    }
}
