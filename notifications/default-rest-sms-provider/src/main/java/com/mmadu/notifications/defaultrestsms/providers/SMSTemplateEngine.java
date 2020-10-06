package com.mmadu.notifications.defaultrestsms.providers;

import com.mmadu.notifications.endpoint.models.NotificationContext;
import reactor.core.publisher.Mono;

public interface SMSTemplateEngine {

    Mono<String> evaluateTemplate(String template, NotificationContext context);
}
