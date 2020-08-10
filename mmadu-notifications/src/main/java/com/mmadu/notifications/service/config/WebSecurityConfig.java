package com.mmadu.notifications.service.config;

import com.mmadu.security.api.MmaduWebSecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class WebSecurityConfig extends MmaduWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/repo/domainNotificationConfigurations/**")
                .hasAuthority("notification_config.read")
                .antMatchers(HttpMethod.GET, "/repo/notificationProfiles/**")
                .hasAuthority("notification_profile.read")
                .anyRequest()
                .authenticated();
    }
}
