package com.mmadu.event.bus.providers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.event.bus.events.Event;
import com.mmadu.event.bus.providers.EventPublisher;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.ExchangeSpecification;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

public class RabbitMQUserEventPublisher implements EventPublisher {
    private String exchange = "user-events";
    private Sender sender;
    private ObjectMapper mapper;

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Mono<Void> initializeExchange() {
        return sender.declareExchange(
                ExchangeSpecification.exchange()
                        .type("topic")
                        .durable(true)
                        .name(exchange)
        ).then();
    }

    @Override
    public <T extends Event> Mono<Void> publishEvent(Publisher<T> events) {
        Flux<OutboundMessage> outboundMessageFlux = Flux.from(events)
                .flatMap(this::convertToOutboundMessage);
        return sender.sendWithPublishConfirms(outboundMessageFlux)
                .then();
    }

    private Mono<OutboundMessage> convertToOutboundMessage(Event event) {
        return Mono.fromCallable(() -> mapper.writeValueAsBytes(event))
                .map(eventBytes -> new OutboundMessage(exchange,
                        String.format("%s.%s", event.getDomain(), event.getType()), eventBytes));
    }
}
