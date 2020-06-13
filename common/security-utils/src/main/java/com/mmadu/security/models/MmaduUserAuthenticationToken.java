package com.mmadu.security.models;

import com.mmadu.security.providers.utils.AuthoritiesUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;

public class MmaduUserAuthenticationToken extends AbstractMmaduAuthenticationToken<AppUser> {
    public MmaduUserAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    @Override
    protected AppUser convertToPrincipal(Jwt jwt) {
        return AppUserImpl.builder()
                .clientId(jwt.getClaimAsString("client_id"))
                .domainId(jwt.getClaimAsString("domain_id"))
                .userId(jwt.getClaimAsString("user_id"))
                .authorities(AuthoritiesUtils.getAuthorities(getAuthorities()))
                .roles(AuthoritiesUtils.getRoles(getAuthorities()))
                .build();
    }


    @Data
    @Builder
    static class AppUserImpl implements AppUser {
        private String domainId;
        private String clientId;
        private String userId;
        private List<String> roles;
        private List<String> authorities;
    }
}
