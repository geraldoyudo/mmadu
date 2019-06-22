package com.mmadu.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MmaduSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DomainTokenChecker.class)
    public DomainTokenChecker domainTokenChecker(
            @Value("${mmadu.tokenService.url}") String tokenServiceUrl,
            @Value("${mmadu.domainKey}") String adminKey
            ) {
        RemoteAppTokenServiceDomainTokenChecker tokenChecker =
                new RemoteAppTokenServiceDomainTokenChecker();
        tokenChecker.setTokenServiceUrl(tokenServiceUrl);
        tokenChecker.setAdminKey(adminKey);
        return tokenChecker;
    }

    @Configuration
    @ConditionalOnProperty(name = "mmadu.domain.api-security-enabled", havingValue = "true", matchIfMissing = true)
    public static class MainConfiguration extends WebSecurityConfigurerAdapter {
        private DomainTokenChecker domainTokenChecker;

        public MainConfiguration(DomainTokenChecker domainTokenChecker) {
            this.domainTokenChecker = domainTokenChecker;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(tokenAuthenticationProvider());
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.expressionHandler(defaultWebSecurityExpressionHandler());
        }

        @Bean
        public PermissionEvaluator domainPermissionEvaluator() {
            DomainPermissionEvaluator evaluator = new DomainPermissionEvaluator();
            evaluator.setDomainTokenChecker(domainTokenChecker);
            return evaluator;
        }

        @Bean
        public TokenAuthenticationFilter tokenAuthenticationFilter() {
            return new TokenAuthenticationFilter();
        }

        @Bean
        public TokenAuthenticationProvider tokenAuthenticationProvider() {
            return new TokenAuthenticationProvider();
        }

        @Bean
        public SpelExpressionParser spelExpressionParser() {
            return new SpelExpressionParser();
        }

        @Bean
        public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
            DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
            handler.setPermissionEvaluator(domainPermissionEvaluator());
            handler.setExpressionParser(spelExpressionParser());
            return handler;
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "mmadu.domain.api-security-enabled", havingValue = "false")
    public static class NoOpConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }

    }
}
