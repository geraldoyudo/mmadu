package com.mmadu.identity.config;

import com.mmadu.security.EnabledWebSecurityConfiguration;
import com.mmadu.security.MmaduSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@EnabledWebSecurityConfiguration
public class WebSecurityConfig implements MmaduSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().access("hasPermission('domain', 'admin')");
    }
}
