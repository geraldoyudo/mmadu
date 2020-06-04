package com.mmadu.identity.config;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.providers.client.authentication.ClientAuthenticationFilter;
import com.mmadu.identity.providers.client.authentication.ClientAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Order(0)
public class ClientApiConfig extends WebSecurityConfigurerAdapter {
    private ClientAuthenticationFilter clientAuthenticationFilter;
    private ClientAuthenticationProvider clientAuthenticationProvider;

    @Autowired
    public void setClientAuthenticationFilter(ClientAuthenticationFilter clientAuthenticationFilter) {
        this.clientAuthenticationFilter = clientAuthenticationFilter;
    }

    @Autowired
    public void setClientAuthenticationProvider(ClientAuthenticationProvider clientAuthenticationProvider) {
        this.clientAuthenticationProvider = clientAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .antMatcher("/clients/**")
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers("/clients/token/**")
                .permitAll()
                .and()
                .authenticationProvider(clientAuthenticationProvider)
                .addFilterBefore(clientAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    public MmaduClient mmaduClient() {
        return (MmaduClient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
