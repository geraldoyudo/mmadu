package com.mmadu.security.api;

import com.mmadu.security.models.MmaduQualified;
import com.mmadu.security.providers.MmaduJwtAuthenticationConverter;
import com.mmadu.security.providers.MmaduWebSecurityExpressionHandler;
import com.mmadu.security.providers.authorization.DomainLimiterFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

public abstract class MmaduWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private JwtDecoder jwtDecoder;
    private MmaduJwtAuthenticationConverter jwtAuthenticationConverter;
    private MmaduWebSecurityExpressionHandler expressionHandler;
    private boolean jwtAutoDiscovery = false;
    private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;
    private DomainLimiterFilter domainLimiterFilter;

    @Value("${mmadu.identity.jwt-auto-discovery:false}")
    public void setJwtAutoDiscovery(boolean jwtAutoDiscovery) {
        this.jwtAutoDiscovery = jwtAutoDiscovery;
    }

    @Autowired(required = false)
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

    @Autowired(required = false)
    public void setAuthenticationManagerResolver(AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) {
        this.authenticationManagerResolver = authenticationManagerResolver;
    }

    @Autowired
    public void setDomainLimiterFilter(DomainLimiterFilter domainLimiterFilter) {
        this.domainLimiterFilter = domainLimiterFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!jwtAutoDiscovery) {
            if (this.jwtDecoder == null) {
                throw new IllegalStateException("no jwt decoder set!");
            }
            http
                    .authorizeRequests()
                    .expressionHandler(expressionHandler)
                    .and()
                    .oauth2ResourceServer(
                            configurer -> configurer.jwt()
                                    .decoder(jwtDecoder)
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter)
                    )
                    .addFilterAfter(domainLimiterFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            if (this.authenticationManagerResolver == null) {
                throw new IllegalStateException("issuer authentication manager resolver does not exist!");
            }
            http
                    .authorizeRequests()
                    .expressionHandler(expressionHandler)
                    .and()
                    .oauth2ResourceServer(
                            configurer -> configurer.authenticationManagerResolver(authenticationManagerResolver)
                    )
                    .addFilterAfter(domainLimiterFilter, UsernamePasswordAuthenticationFilter.class);

        }
    }

}
