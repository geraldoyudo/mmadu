package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationMessage;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class SPELNotificationRuleMatcher implements NotificationRuleMatcher {
    private ExpressionParser parser = new SpelExpressionParser();

    @Override
    public boolean matchesRule(String rule, NotificationMessage message) {
        Expression exp = parser.parseExpression(rule);
        return exp.getValue(message, Boolean.class);
    }
}
