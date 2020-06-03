package com.mmadu.identity.config;

import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.ClientDomainPopulatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

@Configuration
@Order(1000)
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
                .antMatcher("/app/**")
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .authorizeRequests()
                .antMatchers("/app/authorize/**")
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .formLogin()
                .loginPage("/app/login")
                .loginProcessingUrl("/app/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/app/logout")
                .permitAll()
                .and()
                .authenticationProvider(userAuthenticationProvider)
                .addFilterBefore(clientDomainPopulatorFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    public MmaduUser mmaduUser() {
        return (MmaduUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
