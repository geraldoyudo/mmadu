package com.mmadu.service.config;

import com.mmadu.service.security.DomainPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true, 
  securedEnabled = true, 
  jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Bean
    public DefaultMethodSecurityExpressionHandler
    defaultMethodSecurityExpressionHandler(DomainPermissionEvaluator domainPermissionEvaluator){
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(domainPermissionEvaluator);
        return handler;
    }
}