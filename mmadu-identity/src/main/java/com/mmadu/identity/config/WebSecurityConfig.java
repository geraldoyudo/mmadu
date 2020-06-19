package com.mmadu.identity.config;

import com.mmadu.security.api.MmaduWebSecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(10)
public class WebSecurityConfig extends MmaduWebSecurityConfigurer {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .antMatcher("/admin/**")
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/admin/repo/clients/**")
                .hasAuthority("client.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/clientInstances/**")
                .hasAuthority("client_instance.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/domainIdentityConfigurations/**")
                .hasAuthority("identity_config.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/grantAuthorizations/**")
                .hasAuthority("grant_authorization.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/resources/**")
                .hasAuthority("resource.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/scopes/**")
                .hasAuthority("scope.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/tokens/**")
                .hasAuthority("token.read")
                .antMatchers(HttpMethod.GET,"/admin/repo/credentials/**")
                .hasAuthority("credential.read")
                .anyRequest()
                .authenticated();
    }
}
