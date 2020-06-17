package com.mmadu.security.providers;

import com.mmadu.security.providers.converters.JwtAuthenticationConversionStrategy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

public class MmaduJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private List<JwtAuthenticationConversionStrategy> strategies = Collections.emptyList();

    @PostConstruct
    public void init() {
        if (strategies.isEmpty()) {
            throw new IllegalStateException("no jwt conversion strategies set");
        }
    }

    public final AbstractAuthenticationToken convert(Jwt jwt) {
        JwtAuthenticationConversionStrategy strategy = strategies
                .stream()
                .filter(s -> s.apply(jwt))
                .findFirst().orElseThrow(() -> new BadCredentialsException("invalid.jwt.claims"));
        return strategy.convert(jwt);
    }

    public void setStrategies(List<JwtAuthenticationConversionStrategy> strategies) {
        this.strategies = strategies;
    }
}
