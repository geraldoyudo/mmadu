package com.mmadu.service.config;

import com.mmadu.security.EnabledWebSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnabledWebSecurityConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/domains/*/**")
                .access("hasPermission('domain', " +
                        "(request.pathInfo != null? request.pathInfo: request.servletPath).split('/')[2])")
                .anyRequest()
                .access("hasPermission('domain', 'admin')");
    }
}
