package com.mmadu.security.providers;

import com.mmadu.security.providers.domainparsers.DomainParser;
import com.mmadu.security.providers.utils.RequestUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

public class MmaduMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private DomainParser domainParser;
    private String defaultDomainId = "global";

    public void setDefaultDomainId(String defaultDomainId) {
        this.defaultDomainId = defaultDomainId;
    }

    public void setDomainParser(DomainParser domainParser) {
        this.domainParser = domainParser;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        MmaduMethodSecurityExpressionRoot root = new MmaduMethodSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(this.getPermissionEvaluator());
        root.setTrustResolver(this.getTrustResolver());
        root.setRoleHierarchy(this.getRoleHierarchy());
        root.setDefaultRolePrefix(this.getDefaultRolePrefix());

        List<Object> domainPayloads = Arrays.asList(invocation.getArguments());
        RequestUtils.getCurrentRequest()
                .ifPresent(domainPayloads::add);
        String domainId = domainPayloads.stream()
                .flatMap(payload -> domainParser.parseDomain(payload).stream())
                .findFirst()
                .orElse(defaultDomainId);
        root.setDomainId(domainId);
        return root;
    }
}
