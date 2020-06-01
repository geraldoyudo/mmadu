package com.mmadu.identity.config;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.providers.client.authentication.ClientAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Order(300)
public class ClientApiConfig extends WebSecurityConfigurerAdapter {
    private ClientAuthenticationFilter clientAuthenticationFilter;

    @Autowired
    public void setClientAuthenticationFilter(ClientAuthenticationFilter clientAuthenticationFilter) {
        this.clientAuthenticationFilter = clientAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/token/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .addFilterBefore(clientAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    public MmaduClient mmaduClient() {
        return (MmaduClient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
