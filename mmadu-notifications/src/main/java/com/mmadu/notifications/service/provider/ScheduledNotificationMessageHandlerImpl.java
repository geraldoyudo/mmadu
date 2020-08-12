package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.entities.ScheduledUserNotificationMessage;
import com.mmadu.notifications.service.models.GenericUserEvent;
import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.repositories.ScheduledUserNotificationMessageRepository;
import com.mmadu.notifications.service.services.NotificationService;
import com.mmadu.notifications.service.utils.Pair;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class ScheduledNotificationMessageHandlerImpl implements ScheduledNotificationMessageHandler {
    private String domainId;
    private ScheduledUserNotificationMessageRepository scheduledUserNotificationMessageRepository;
    private UserService userService;
    private ExpressionParser expressionParser;
    private NotificationService notificationService;

    public void setUserService(UserService userService) {
        this.userService = userService;
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
    public void handleEvent(GenericUserEvent event) {
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

    private Mono<Boolean> eventMatches(String filter, GenericUserEvent event) {
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
                .map(m -> toNotificationMessage(m, user, event))
                .flatMap(m -> this.notificationService.send(m));
    }

    private SendNotificationMessageRequest toNotificationMessage(ScheduledUserNotificationMessage message,
                                                                 NotificationUser user, GenericUserEvent event) {
        SendNotificationMessageRequest request = new SendNotificationMessageRequest();
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

}
