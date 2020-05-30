package com.mmadu.identity.config;

import com.mmadu.identity.providers.authorization.ClientDomainPopulatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(200)
public class AuthorizeEndpointConfig extends WebSecurityConfigurerAdapter {
    private AuthenticationProvider userAuthenticationProvider;
    private ClientDomainPopulatorFilter clientDomainPopulatorFilter;

    @Autowired
    public void setClientDomainPopulatorFilter(ClientDomainPopulatorFilter clientDomainPopulatorFilter) {
        this.clientDomainPopulatorFilter = clientDomainPopulatorFilter;
    }

    @Autowired
    @Qualifier("mmaduUser")
    public void setUserAuthenticationProvider(AuthenticationProvider userAuthenticationProvider) {
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/authorize/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .authenticationProvider(userAuthenticationProvider)
                .addFilterBefore(clientDomainPopulatorFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
