package com.mmadu.service.config;

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
                .antMatchers(HttpMethod.GET, "/appDomains/**")
                .hasAuthority("domain.read")
                .antMatchers(HttpMethod.DELETE, "/appDomains/**")
                .hasAuthority("domain.delete")
                .antMatchers(HttpMethod.POST, "/appDomains")
                .hasAuthority("domain.create")
                .antMatchers(HttpMethod.GET, "/appUsers/**")
                .hasAuthority("user.read")
                .antMatchers(HttpMethod.GET, "/authorities/**")
                .hasAuthority("authority.read")
                .antMatchers(HttpMethod.GET, "/groups/**")
                .hasAuthority("group.read")
                .antMatchers(HttpMethod.GET, "/roleAuthorities/**")
                .hasAuthority("role_authority.read")
                .antMatchers(HttpMethod.GET, "/userAuthorities/**")
                .hasAuthority("user_authority.read")
                .antMatchers(HttpMethod.GET, "/userGroups/**")
                .hasAuthority("user_group.read")
                .antMatchers(HttpMethod.GET, "/userRoles/**")
                .hasAuthority("user_role.read")
                .anyRequest()
                .authenticated();
    }
}
