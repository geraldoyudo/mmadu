package com.mmadu.event.bus.providers.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.Sender;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Disabled
class RabbitMQUserEventPublisherTest {

    @Test
    void testSendMessage() throws Exception {
        Sender sender = RabbitFlux.createSender();
        RabbitMQUserEventPublisher publisher = new RabbitMQUserEventPublisher();
        publisher.setMapper(new ObjectMapper());
        publisher.setSender(sender);
        Receiver receiver = RabbitFlux.createReceiver();
        CountDownLatch latch = new CountDownLatch(1);
        RabbitMQUserEventSubscriber testEventSubscriber = new RabbitMQUserEventSubscriber();
        testEventSubscriber.setDomainId("1");
        testEventSubscriber.setMapper(new ObjectMapper());
        testEventSubscriber.setReceiver(receiver);
        testEventSubscriber.setSender(sender);
        testEventSubscriber.setType("user.test");
        testEventSubscriber.receiveEventsFor(TestEvent.class)
                .subscribe(event -> {
                    System.out.println(event);
                    latch.countDown();
                });

        TestEvent event = getTestEvent();
        publisher.publishEvent(Mono.just(event))
                .delaySubscription(publisher.initializeExchange())
                .block();

        latch.await(3, TimeUnit.SECONDS);
    }

    public static TestEvent getTestEvent() {
        TestEvent event = new TestEvent();
        event.setDomain("1");
        event.setId("1323");
        event.setKey("key");
        event.setUserId("111");
        return event;
    }

}