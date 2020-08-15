package com.mmadu.security.providers.authorization;

import com.mmadu.security.providers.domainparsers.TenantExtractor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import javax.servlet.http.HttpServletRequest;

public final class TenantBasedAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {
    private AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver;
    private TenantExtractor tenantExtractor;
    private String defaultDomain = "0";

    public AuthenticationManager resolve(HttpServletRequest request) {
        String domain = this.tenantExtractor.getDomain(request)
                .orElse(defaultDomain);
        AuthenticationManager authenticationManager = this.issuerAuthenticationManagerResolver.resolve(domain);
        if (authenticationManager == null) {
            throw new InvalidBearerTokenException("Invalid domain");
        } else {
            return authenticationManager;
        }
    }

    public void setIssuerAuthenticationManagerResolver(AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver) {
        this.issuerAuthenticationManagerResolver = issuerAuthenticationManagerResolver;
    }

    public void setTenantExtractor(TenantExtractor tenantExtractor) {
        this.tenantExtractor = tenantExtractor;
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }
}
