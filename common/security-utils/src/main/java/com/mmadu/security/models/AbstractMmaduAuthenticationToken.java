package com.mmadu.security.models;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public abstract class AbstractMmaduAuthenticationToken<T> extends AbstractAuthenticationToken {
    private T principal;
    private Jwt credentials;

    public AbstractMmaduAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = convertToPrincipal(jwt);
        this.credentials = jwt;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    protected abstract T convertToPrincipal(Jwt jwt);
}
