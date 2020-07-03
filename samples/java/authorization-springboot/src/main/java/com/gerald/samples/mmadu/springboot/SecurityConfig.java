package com.gerald.samples.mmadu.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtUserService jwtUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/edit.html")
                .hasAuthority("edit")
                .anyRequest()
                .hasAuthority("view")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(jwtUserService);
    }
}
