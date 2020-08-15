package com.mmadu.security.providers.authorization;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TenantBasedAuthenticationResolver implements AuthenticationManagerResolver<String> {
    private Map<String, AuthenticationManager> authenticationProviderMap = new ConcurrentHashMap<>();

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;
    private List<DomainJwkSetUriResolver> domainJwkSetUriResolvers = Collections.emptyList();

    public void setJwtAuthenticationConverter(Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    public void setDomainJwkSetUriResolvers(List<DomainJwkSetUriResolver> domainJwkSetUriResolvers) {
        this.domainJwkSetUriResolvers = domainJwkSetUriResolvers;
    }

    @Override
    public AuthenticationManager resolve(String issuer) {
        return authenticationProviderMap.computeIfAbsent(issuer, this::createAuthenticationManagerForIssuer);
    }

    private AuthenticationManager createAuthenticationManagerForIssuer(String issuer) {
        return getJwkSetUriForIssuer(issuer)
                .map(jwksetUri -> NimbusJwtDecoder.withJwkSetUri(jwksetUri).build())
                .map(this::createNewJwtAuthenticationProvider)
                .map(provider -> new AuthenticationManager() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        return provider.authenticate(authentication);
                    }
                })
                .orElseThrow(() -> new InvalidBearerTokenException("Invalid issuer"));
    }

    private Optional<String> getJwkSetUriForIssuer(String issuer) {
        for (DomainJwkSetUriResolver resolver : domainJwkSetUriResolvers) {
            Optional<String> jwkSetUri = resolver.getJwkSetUriForDomain(issuer);
            if (jwkSetUri.isPresent()) {
                return jwkSetUri;
            }
        }
        return Optional.empty();
    }

    private JwtAuthenticationProvider createNewJwtAuthenticationProvider(NimbusJwtDecoder decoder) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        return provider;
    }
}
