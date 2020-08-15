package com.mmadu.security.providers.domainparsers;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class TenantBasedDomainExtractor implements HttpRequestDomainExtractor {
    private TenantExtractor tenantExtractor;

    public void setTenantExtractor(TenantExtractor tenantExtractor) {
        this.tenantExtractor = tenantExtractor;
    }

    @Override
    public Optional<String> extractDomainIdFromRequest(HttpServletRequest request) {
        return tenantExtractor.getDomain(request);
    }
}
