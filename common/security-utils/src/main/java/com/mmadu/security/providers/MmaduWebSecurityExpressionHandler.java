package com.mmadu.security.providers;

import com.mmadu.security.providers.domainparsers.DomainParser;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

public class MmaduWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
    private DomainParser domainParser;
    private String defaultDomainId = "global";

    public void setDefaultDomainId(String defaultDomainId) {
        this.defaultDomainId = defaultDomainId;
    }

    public void setDomainParser(DomainParser domainParser) {
        this.domainParser = domainParser;
    }

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        MmaduWebSecurityExpressionRoot root = new MmaduWebSecurityExpressionRoot(authentication, fi);
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        String domainId = domainParser.parseDomain(fi.getHttpRequest())
                .orElse(defaultDomainId);
        root.setDomainId(domainId);
        return root;
    }
}
