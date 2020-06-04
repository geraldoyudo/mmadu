package com.mmadu.identity.providers.client.authentication;

import com.mmadu.identity.models.client.MmaduClient;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class MmaduClientAuthenticationToken extends AbstractAuthenticationToken {
    private MmaduClient client;

    public MmaduClientAuthenticationToken(MmaduClient client) {
        super(
                Optional.ofNullable(client.getAuthorities()).orElse(Collections.emptyList())
                        .stream()
                        .map(a -> new SimpleGrantedAuthority(a))
                        .collect(Collectors.toList())
        );
        this.client = client;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return client;
    }
}
