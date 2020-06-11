package com.mmadu.security;

import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

public class MmaduWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private final String defaultRolePrefix = "ROLE_";

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        MmaduWebSecurityExpressionRoot root = new MmaduWebSecurityExpressionRoot(authentication, fi);
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(this.getRoleHierarchy());
        root.setDefaultRolePrefix(this.defaultRolePrefix);
        return root;
    }
}
