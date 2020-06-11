package com.mmadu.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.servlet.http.HttpServletRequest;

public class MmaduWebSecurityExpressionRoot extends MmaduSecurityExpressionRoot {
    private HttpServletRequest request;

    public MmaduWebSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
        super(authentication);
        this.request = fi.getRequest();
    }

    public boolean hasIpAddress(String ipAddress) {
        return (new IpAddressMatcher(ipAddress)).matches(this.request);
    }
}
