package com.mmadu.event.bus.providers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.event.bus.providers.EventSubscriber;
import com.mmadu.event.bus.providers.EventSubscriberFactory;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;


public class RabbitMQUserEventSubscriberFactory implements EventSubscriberFactory {
    private ObjectMapper mapper;
    private Receiver receiver;
    private Sender sender;
    private String exchange;

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public EventSubscriber createSubscription(String domainId, String eventType) {
        RabbitMQUserEventSubscriber subscriber = new RabbitMQUserEventSubscriber();
        subscriber.setMapper(mapper);
        subscriber.setSender(sender);
        subscriber.setReceiver(receiver);
        subscriber.setExchange(exchange);
        subscriber.setType(eventType);
        subscriber.setDomainId(domainId);
        return subscriber;
    }
}
