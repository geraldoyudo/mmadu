package com.mmadu.identity.providers.authorization;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MmaduUserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String domainId;

    public MmaduUserAuthenticationToken(Object principal, Object credentials, String domainId) {
        super(principal, credentials);
        this.domainId = domainId;
    }

    public MmaduUserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String domainId) {
        super(principal, credentials, authorities);
        this.domainId = domainId;
    }
}
