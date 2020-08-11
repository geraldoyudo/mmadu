package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.service.repositories.ScheduledNotificationMessageRepository;
import com.mmadu.notifications.service.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class ScheduledNotificationMessageHandlerFactoryImpl implements
        ScheduledNotificationMessageHandlerFactory {

    private ScheduledNotificationMessageRepository scheduledNotificationMessageRepository;
    private UserService userService;
    private ExpressionParser expressionParser = new SpelExpressionParser();
    private NotificationService notificationService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setScheduledNotificationMessageRepository(ScheduledNotificationMessageRepository scheduledNotificationMessageRepository) {
        this.scheduledNotificationMessageRepository = scheduledNotificationMessageRepository;
    }

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @Override
    public ScheduledNotificationMessageHandler getHandlerForDomain(String domainId) {
        ScheduledNotificationMessageHandlerImpl handler = new ScheduledNotificationMessageHandlerImpl();
        handler.setDomainId(domainId);
        handler.setExpressionParser(expressionParser);
        handler.setNotificationService(notificationService);
        handler.setScheduledNotificationMessageRepository(scheduledNotificationMessageRepository);
        handler.setUserService(userService);
        return handler;
    }
}
