package com.mmadu.service.config;

import com.mmadu.service.security.DomainPermissionEvaluator;
import com.mmadu.service.security.TokenAuthenticationFilter;
import com.mmadu.service.security.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DomainPermissionEvaluator domainPermissionEvaluator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/domains/*/**")
                .access("hasPermission('domain', " +
                        "(request.pathInfo != null? request.pathInfo: request.servletPath).split('/')[2])")
                .anyRequest()
                .access("hasPermission('domain', 'admin')")
                .and()
                .addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(tokenAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.expressionHandler(defaultWebSecurityExpressionHandler());
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

    @Bean
    public TokenAuthenticationProvider tokenAuthenticationProvider(){
        return new TokenAuthenticationProvider();
    }

    @Bean
    public SpelExpressionParser spelExpressionParser(){
        return new SpelExpressionParser();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(domainPermissionEvaluator);
        handler.setExpressionParser(spelExpressionParser());
        return handler;
    }
}
