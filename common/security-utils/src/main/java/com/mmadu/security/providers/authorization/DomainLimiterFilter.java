package com.mmadu.security.providers.authorization;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class DomainLimiterFilter implements Filter {
    private DomainLimiter domainLimiter;

    public void setDomainLimiter(DomainLimiter domainLimiter) {
        this.domainLimiter = domainLimiter;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            domainLimiter.limitRequestToDomain((HttpServletRequest) servletRequest);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
