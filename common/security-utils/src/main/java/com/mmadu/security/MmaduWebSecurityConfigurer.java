package com.mmadu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public abstract class MmaduWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private JwtDecoder jwtDecoder;
    private MmaduJwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    @Qualifier("mmaduIdentity")
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Autowired
    @Qualifier("mmaduIdentity")
    public void setJwtAuthenticationConverter(MmaduJwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(
                        configurer -> configurer.jwt()
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                );
    }
}
