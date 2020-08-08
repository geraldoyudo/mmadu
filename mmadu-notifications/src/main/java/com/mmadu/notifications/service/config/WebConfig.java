package com.mmadu.notifications.service.config;

import com.mmadu.notifications.service.DomainNotificationConfigurationList;
import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableConfigurationProperties(DomainNotificationConfigurationList.class)
public class WebConfig {

    @Bean
    @Qualifier("web")
    public Validator webValidator() {
        return new LocalValidatorFactoryBean();
    }

}
