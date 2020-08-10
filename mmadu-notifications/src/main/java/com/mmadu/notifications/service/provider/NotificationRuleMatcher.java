package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationMessage;

public interface NotificationRuleMatcher {

    boolean matchesRule(String rule, NotificationMessage message);
}
