package com.mmadu.notifications.defaultrestsms;

import com.mmadu.notifications.defaultrestsms.models.DefaultRestSMSProperties;
import com.mmadu.notifications.defaultrestsms.providers.*;
import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@EnableConfigurationProperties(DefaultRestSMSProperties.class)
public class DefaultRestSMSAutoConfiguration {
    @Bean
    public NotificationProviderRegistration restSMSProviderRegistration(
            WebClientResolver resolver, SMSTemplateEngine engine, DefaultRestSMSProperties properties
    ) {
        DefaultRestSMSProvider provider = new DefaultRestSMSProvider();
        provider.setDefaultProperties(properties);
        provider.setResolver(resolver);
        provider.setTemplateEngine(engine);
        return NotificationProviderRegistration.builder()
                .id("rest-sms")
                .provider(provider)
                .build();
    }

    @Bean
    public WebClientResolver defaultWebClientResolver(DefaultRestSMSProperties properties) {
        DefaultWebClientResolverImpl resolver = new DefaultWebClientResolverImpl();
        resolver.setDefaultProperties(properties);
        return resolver;
    }

    @Bean
    public SMSTemplateEngine velocitySMSTemplateEngine(VelocityEngine engine) {
        VelocitySMSTemplateEngine smsTemplateEngine = new VelocitySMSTemplateEngine();
        smsTemplateEngine.setEngine(engine);
        return smsTemplateEngine;
    }

    @Bean
    @ConditionalOnMissingBean
    public VelocityEngine velocityEngine() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine engine = new VelocityEngine();
        engine.init(p);
        return engine;
    }
}
