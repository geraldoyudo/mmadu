package com.mmadu.security.providers.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class AuthoritiesUtils {

    private AuthoritiesUtils() {

    }

    public static List<String> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return Optional.ofNullable(authorities)
                .orElse(Collections.emptyList())
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("a."))
                .map(auth -> auth.substring(2))
                .collect(Collectors.toList());
    }

    public static List<String> getRoles(Collection<? extends GrantedAuthority> authorities) {
        return Optional.ofNullable(authorities)
                .orElse(Collections.emptyList())
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("r."))
                .map(auth -> auth.substring(2))
                .collect(Collectors.toList());
    }

    public static Collection<? extends GrantedAuthority> getGrantedAuthoritiesFromClaim(String claimName, Jwt jwt) {
        return getAuthoritiesFromClaim(claimName, jwt)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static Collection<String> getAuthoritiesFromClaim(String claimName, Jwt jwt) {
        if (claimName == null) {
            return Collections.emptyList();
        } else {
            Object authorities = jwt.getClaim(claimName);
            if (authorities instanceof String) {
                return StringUtils.hasText((String) authorities) ? Arrays.asList(((String) authorities).split(" ")) : Collections.emptyList();
            } else {
                return (Collection) (authorities instanceof Collection ? (Collection) authorities : Collections.emptyList());
            }
        }
    }
}
