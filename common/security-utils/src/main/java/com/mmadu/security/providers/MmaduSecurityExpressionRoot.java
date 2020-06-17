package com.mmadu.security.providers;

import com.mmadu.security.providers.permissions.AntStylePermissionMatcher;
import com.mmadu.security.providers.permissions.PermissionMatcher;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MmaduSecurityExpressionRoot implements SecurityExpressionOperations {
    protected final Authentication authentication;
    private AuthenticationTrustResolver trustResolver;
    private PermissionMatcher permissionMatcher = new AntStylePermissionMatcher();
    private Set<String> roles;
    private String defaultRolePrefix = "r.";
    private String defaultAuthorityPrefix = "a.";
    public final boolean permitAll = true;
    public final boolean denyAll = false;
    private PermissionEvaluator permissionEvaluator;
    public final String read = "read";
    public final String write = "write";
    public final String create = "create";
    public final String delete = "delete";
    public final String admin = "administration";
    private String domainId = "global";
    private Set<String> authorities;

    public MmaduSecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        } else {
            this.authentication = authentication;
        }
    }

    public final boolean hasAuthority(String authority) {
        return this.hasAnyAuthority(authority);
    }

    public final boolean hasAnyAuthority(String... authorities) {
        return this.hasAnyAuthorityName(authorities);
    }

    public final boolean hasRole(String role) {
        return this.hasAnyRole(role);
    }

    public final boolean hasAnyRole(String... roles) {
        return this.hasAnyRoleName(roles);
    }

    private boolean hasAnyAuthorityName(String... authorities) {
        Set<String> authoritySet = getAuthoritySet();
        for (String authorityInQuestion : authorities) {
            for (String availableAuthority : authoritySet) {
                if (permissionMatcher.matchesPermission(availableAuthority,
                        domainId + "." + authorityInQuestion)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAnyRoleName(String... roles) {
        Set<String> roleSet = getRoleSet();
        for (String roleInQuestion : roles) {
            for (String availableRole : roleSet) {
                if (permissionMatcher.matchesPermission(availableRole,
                        domainId + "." + roleInQuestion)) {
                    return true;
                }
            }
        }
        return false;
    }

    public final Authentication getAuthentication() {
        return this.authentication;
    }

    public final boolean permitAll() {
        return true;
    }

    public final boolean denyAll() {
        return false;
    }

    public final boolean isAnonymous() {
        return this.trustResolver.isAnonymous(this.authentication);
    }

    public final boolean isAuthenticated() {
        return !this.isAnonymous();
    }

    public final boolean isRememberMe() {
        return this.trustResolver.isRememberMe(this.authentication);
    }

    public final boolean isFullyAuthenticated() {
        return !this.trustResolver.isAnonymous(this.authentication) && !this.trustResolver.isRememberMe(this.authentication);
    }

    public Object getPrincipal() {
        return this.authentication.getPrincipal();
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }

    public void setDefaultRolePrefix(String defaultRolePrefix) {
        this.defaultRolePrefix = defaultRolePrefix;
    }

    public void setDefaultAuthorityPrefix(String defaultAuthorityPrefix) {
        this.defaultAuthorityPrefix = defaultAuthorityPrefix;
    }

    private Set<String> getAuthoritySet() {
        if (this.authorities == null) {
            Collection<? extends GrantedAuthority> userAuthorities = this.authentication.getAuthorities()
                    .stream()
                    .filter(authority -> authority.getAuthority().startsWith(defaultAuthorityPrefix))
                    .collect(Collectors.toList());
            this.authorities = AuthorityUtils.authorityListToSet(userAuthorities)
                    .stream()
                    .map(auth -> auth.substring(2))
                    .collect(Collectors.toSet());
        }
        return this.authorities;
    }

    private Set<String> getRoleSet() {
        if (this.roles == null) {
            Collection<? extends GrantedAuthority> userAuthorities = this.authentication.getAuthorities()
                    .stream()
                    .filter(authority -> authority.getAuthority().startsWith(defaultRolePrefix))
                    .collect(Collectors.toList());
            this.roles = AuthorityUtils.authorityListToSet(userAuthorities)
                    .stream()
                    .map(auth -> auth.substring(2))
                    .collect(Collectors.toSet());
        }
        return this.roles;
    }

    public boolean hasPermission(Object target, Object permission) {
        return this.permissionEvaluator.hasPermission(this.authentication, target, permission);
    }

    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return this.permissionEvaluator.hasPermission(this.authentication, (Serializable) targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return role;
        } else if (defaultRolePrefix != null && defaultRolePrefix.length() != 0) {
            return role.startsWith(defaultRolePrefix) ? role : defaultRolePrefix + role;
        } else {
            return role;
        }
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setPermissionMatcher(PermissionMatcher permissionMatcher) {
        this.permissionMatcher = permissionMatcher;
    }
}
