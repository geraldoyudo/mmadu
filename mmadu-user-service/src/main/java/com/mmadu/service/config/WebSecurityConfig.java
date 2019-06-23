package com.mmadu.service.config;

import com.mmadu.security.EnabledWebSecurityConfiguration;
import com.mmadu.security.MmaduSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@EnabledWebSecurityConfiguration
public class WebSecurityConfig implements MmaduSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
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
