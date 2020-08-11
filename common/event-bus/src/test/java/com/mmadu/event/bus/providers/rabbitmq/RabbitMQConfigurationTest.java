package com.mmadu.event.bus.providers.rabbitmq;

import com.mmadu.event.bus.EventBusConfiguration;
import com.mmadu.event.bus.providers.EventPublisher;
import com.mmadu.event.bus.providers.EventSubscriberFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Disabled
@SpringBootTest(classes = EventBusConfiguration.class)
public class RabbitMQConfigurationTest {
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private EventSubscriberFactory eventSubscriberFactory;

    @Test
    void testSendAndReceive() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        eventSubscriberFactory.createSubscription("1", "user.test")
                .receiveEventsFor(TestEvent.class)
                .subscribe(event -> {
                    System.out.println(event);
                    latch.countDown();
                });
        eventPublisher.publishEvent(Mono.just(RabbitMQUserEventPublisherTest.getTestEvent()))
                .block();
        latch.await(3, TimeUnit.SECONDS);
    }
}
