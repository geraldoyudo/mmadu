package com.mmadu.notifications.defaultrestsms.providers;

import com.mmadu.notifications.endpoint.models.NotificationContext;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import reactor.core.publisher.Mono;

import java.io.StringWriter;

public class VelocitySMSTemplateEngine implements SMSTemplateEngine {
    private VelocityEngine engine;

    @Override
    public Mono<String> evaluateTemplate(String template, NotificationContext context) {
        return Mono.fromCallable(() -> processTemplate(template, context));
    }

    private String processTemplate(String template, NotificationContext context) {
        StringWriter writer = new StringWriter();
        engine.evaluate(new VelocityContext(context.getAsMap()), writer, "", template);
        return writer.toString();
    }

    public void setEngine(VelocityEngine engine) {
        this.engine = engine;
    }
}
