package com.mmadu.security.api;

import com.mmadu.security.models.MmaduQualified;
import com.mmadu.security.providers.MmaduJwtAuthenticationConverter;
import com.mmadu.security.providers.MmaduWebSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public abstract class MmaduWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private JwtDecoder jwtDecoder;
    private MmaduJwtAuthenticationConverter jwtAuthenticationConverter;
    private MmaduWebSecurityExpressionHandler expressionHandler;

    @Autowired
    @MmaduQualified
    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Autowired
    @MmaduQualified
    public void setJwtAuthenticationConverter(MmaduJwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Autowired
    @MmaduQualified
    public void setExpressionHandler(MmaduWebSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .expressionHandler(expressionHandler)
                .and()
                .oauth2ResourceServer(
                        configurer -> configurer.jwt()
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                );
    }

}
