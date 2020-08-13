package com.mmadu.notifications.service.provider;

import com.mmadu.event.bus.events.Event;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.entities.ScheduledEventNotificationMessage;
import com.mmadu.notifications.service.entities.ScheduledUserNotificationMessage;
import com.mmadu.notifications.service.models.GenericEvent;
import com.mmadu.notifications.service.models.GenericUserEvent;
import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.models.SendUserNotificationMessageRequest;
import com.mmadu.notifications.service.repositories.ScheduledEventNotificationMessageRepository;
import com.mmadu.notifications.service.repositories.ScheduledUserNotificationMessageRepository;
import com.mmadu.notifications.service.services.NotificationService;
import com.mmadu.notifications.service.utils.Pair;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class ScheduledNotificationMessageHandlerImpl implements ScheduledNotificationMessageHandler {
    private String domainId;
    private ScheduledUserNotificationMessageRepository scheduledUserNotificationMessageRepository;
    private ScheduledEventNotificationMessageRepository scheduledEventNotificationMessageRepository;
    private UserService userService;
    private ExpressionParser expressionParser;
    private NotificationService notificationService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setScheduledEventNotificationMessageRepository(ScheduledEventNotificationMessageRepository scheduledEventNotificationMessageRepository) {
        this.scheduledEventNotificationMessageRepository = scheduledEventNotificationMessageRepository;
    }

    public void setScheduledNotificationMessageRepository(ScheduledUserNotificationMessageRepository scheduledUserNotificationMessageRepository) {
        this.scheduledUserNotificationMessageRepository = scheduledUserNotificationMessageRepository;
    }

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public void handleUserEvent(GenericUserEvent event) {
        Mono.fromCallable(() ->
                scheduledUserNotificationMessageRepository.findByDomainIdAndEventTriggersContains(
                        domainId, event.getType()))
                .flatMap(
                        list -> userService.findByUserIdAndDomain(event.getUserId(), domainId)
                                .map(user -> new Pair<>(user, list))
                )
                .flux()
                .flatMap(
                        userListPair ->
                                Flux.fromIterable(userListPair.getSecond())
                                        .map(message -> new Pair<>(userListPair.getFirst(), message)
                                        ))
                .filterWhen(pair -> this.eventMatches(pair.getSecond().getEventFilter(), event))
                .filterWhen(pair -> this.userMatches(pair.getSecond().getUserFilter(), pair.getFirst()))
                .flatMap(pair -> this.notifyUser(pair.getSecond(), pair.getFirst(), event))
                .subscribe();
    }

    private Mono<Boolean> eventMatches(String filter, Event event) {
        if (StringUtils.isEmpty(filter)) {
            return Mono.just(true);
        } else {
            return Mono.fromCallable(() -> expressionParser.parseExpression(filter))
                    .flatMap(expression -> Mono.fromCallable(() -> expression.getValue(event, Boolean.class)));
        }
    }

    private Mono<Boolean> userMatches(String filter, NotificationUser user) {
        if (StringUtils.isEmpty(filter)) {
            return Mono.just(true);
        } else {
            return Mono.fromCallable(() -> expressionParser.parseExpression(filter))
                    .flatMap(expression -> Mono.fromCallable(() -> expression.getValue(user, Boolean.class)));
        }
    }

    private Mono<Void> notifyUser(ScheduledUserNotificationMessage message, NotificationUser user, GenericUserEvent event) {
        return Mono.just(message)
                .map(m -> toUserNotificationMessage(m, user, event))
                .flatMap(m -> this.notificationService.sendToUser(m));
    }

    private SendUserNotificationMessageRequest toUserNotificationMessage(ScheduledUserNotificationMessage message,
                                                                         NotificationUser user, GenericUserEvent event) {
        SendUserNotificationMessageRequest request = new SendUserNotificationMessageRequest();
        request.setMessageTemplate(message.getMessageTemplate());
        request.setMessageContent(message.getMessage());
        request.setProfileId(message.getProfile());
        request.setDomainId(message.getDomainId());
        request.setHeaders(message.getHeaders());
        Map<String, Object> context = new HashMap<>(message.getContext());
        context.put("event", event);
        request.setContext(context);
        request.setUserId(user.getId());
        request.setType(message.getType());
        return request;
    }

    @Override
    public void handleEvent(GenericEvent event) {
        Mono.fromCallable(() ->
                scheduledEventNotificationMessageRepository.findByDomainIdAndEventTriggersContains(
                        domainId, event.getType()))
                .flux()
                .flatMap(list -> Flux.fromIterable(list))
                .filterWhen(item -> this.eventMatches(item.getEventFilter(), event))
                .flatMap(item -> this.notifyEvent(item, event))
                .subscribe();
    }

    private Mono<Void> notifyEvent(ScheduledEventNotificationMessage message, GenericEvent event) {
        return Mono.just(message)
                .map(m -> toNotificationMessage(m, event))
                .flatMap(m -> this.notificationService.send(m));
    }

    private SendNotificationMessageRequest toNotificationMessage(ScheduledEventNotificationMessage message,
                                                                 GenericEvent event) {
        SendNotificationMessageRequest request = new SendNotificationMessageRequest();
        request.setMessageTemplate(message.getMessageTemplate());
        request.setMessageContent(message.getMessage());
        request.setProfileId(message.getProfile());
        request.setDomainId(message.getDomainId());
        request.setHeaders(message.getHeaders());
        Map<String, Object> context = new HashMap<>(message.getContext());
        context.put("event", event);
        request.setContext(context);
        request.setType(message.getType());
        Object destination;
        if (StringUtils.isEmpty(message.getDestinationExpression())) {
            Map<String, Object> headers = Optional.ofNullable(message.getHeaders()).orElse(Collections.emptyMap());
            destination = headers.getOrDefault("destination", emptyList());
        } else {
            Expression expression = expressionParser.parseExpression(message.getDestinationExpression());
            destination = expression.getValue(event);
        }
        if (destination instanceof List) {
            List<Object> list = (List<Object>) destination;
            request.setDestination(list.stream().map(Object::toString).collect(Collectors.toList()));
        } else if (destination != null) {
            request.setDestination(Collections.singletonList(destination.toString()));
        } else {
            request.setDestination(emptyList());
        }
        return request;
    }
}
