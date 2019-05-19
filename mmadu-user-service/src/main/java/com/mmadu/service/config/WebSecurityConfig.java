package com.mmadu.service.config;

import com.mmadu.service.security.DomainIdExtractorFilter;
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
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DomainPermissionEvaluator domainPermissionEvaluator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/appUsers").access("hasPermission('domain', 'admin')")
                .antMatchers(HttpMethod.GET, "/appUsers/*").access("hasPermission('user', request.pathInfo.split('/')[2])")
                .antMatchers(HttpMethod.DELETE, "/appUsers/*").access("hasPermission('user', request.pathInfo.split('/')[2])")
                .antMatchers(HttpMethod.PATCH, "/appUsers/*").access("hasPermission('user', request.pathInfo.split('/')[2])")
                .antMatchers(HttpMethod.POST, "/appUsers").access("hasPermission('domain', request.getAttribute('domainId'))")
                .antMatchers(HttpMethod.GET, "/appUsers/search/*DomainId")
                .access("hasPermission('domain', request.getParameterValues('domainId')[0])")
                .anyRequest()
                .permitAll()
                .and()
                .addFilterAfter(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(commonsRequestLoggingFilter(), TokenAuthenticationFilter.class)
                .addFilterAfter(domainIdExtractorFilter(), CommonsRequestLoggingFilter.class)
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

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter(){
        return new CommonsRequestLoggingFilter();
    }

    @Bean
    public DomainIdExtractorFilter domainIdExtractorFilter(){
        return new DomainIdExtractorFilter();
    }
}
