package com.mmadu.security.models;

import com.mmadu.security.providers.utils.AuthoritiesUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;

public class MmaduClientAuthenticationToken extends AbstractMmaduAuthenticationToken<AppClient> {
    public MmaduClientAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        setAuthenticated(true);
    }

    @Override
    protected AppClient convertToPrincipal(Jwt jwt) {
        return AppClientImpl.builder()
                .clientId(jwt.getClaimAsString("client_id"))
                .domainId(jwt.getClaimAsString("domain_id"))
                .authorities(AuthoritiesUtils.getAuthorities(getAuthorities()))
                .roles(AuthoritiesUtils.getRoles(getAuthorities()))
                .build();
    }

    @Data
    @Builder
    static class AppClientImpl implements AppClient {
        private String domainId;
        private String clientId;
        private List<String> authorities;
        private List<String> roles;
    }
}
