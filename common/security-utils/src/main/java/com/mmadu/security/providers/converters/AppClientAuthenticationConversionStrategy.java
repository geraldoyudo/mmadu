package com.mmadu.security.providers.converters;

import com.mmadu.security.models.MmaduClientAuthenticationToken;
import com.mmadu.security.providers.utils.AuthoritiesUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class AppClientAuthenticationConversionStrategy implements JwtAuthenticationConversionStrategy {
    @Override
    public boolean apply(Jwt jwt) {
        return jwt.containsClaim("domain_id") && jwt.containsClaim("client_id");
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new MmaduClientAuthenticationToken(jwt, deriveAuthorities(jwt));
    }

    private Collection<? extends GrantedAuthority> deriveAuthorities(Jwt jwt) {
        return AuthoritiesUtils.getGrantedAuthoritiesFromClaim("authorities", jwt);
    }
}
