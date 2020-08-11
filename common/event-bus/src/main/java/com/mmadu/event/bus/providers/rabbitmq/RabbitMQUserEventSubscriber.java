package com.mmadu.event.bus.providers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.event.bus.events.Event;
import com.mmadu.event.bus.providers.EventSubscriber;
import com.rabbitmq.client.Delivery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import static reactor.rabbitmq.BindingSpecification.binding;
import static reactor.rabbitmq.ExchangeSpecification.exchange;
import static reactor.rabbitmq.QueueSpecification.queue;

public class RabbitMQUserEventSubscriber implements EventSubscriber {
    private String domainId = "*";
    private String exchange = "user-events";
    private String type;
    private Sender sender;
    private Receiver receiver;
    private ObjectMapper mapper;

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Mono<Void> init() {
        String queueName = String.format("UserEventQueue[%s.%s]", domainId, type);
        String routingKey = String.format("%s.%s", domainId, type);
        return sender.declare(exchange(exchange).type("topic").durable(true))
                .then(sender.declare(queue(queueName).durable(true)))
                .then(sender.bind(binding(exchange, routingKey, queueName)))
                .then();
    }

    @Override
    public <T extends Event> Flux<T> receiveEventsFor(Class<T> clazz) {
        String queueName = String.format("UserEventQueue[%s.%s]", domainId, type);
        return receiver.consumeAutoAck(queueName)
                .delaySubscription(this.init())
                .flatMap(d -> this.convertToModel(d, clazz));
    }

    private <T> Mono<T> convertToModel(Delivery delivery, Class<T> clazz) {
        return Mono.fromCallable(() -> mapper.readValue(delivery.getBody(), clazz));
    }
}
