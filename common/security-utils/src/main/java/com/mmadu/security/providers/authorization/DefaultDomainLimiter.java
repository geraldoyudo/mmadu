package com.mmadu.security.providers.authorization;

import com.mmadu.security.providers.domainparsers.DomainExtractor;
import com.mmadu.security.providers.domainparsers.TenantExtractor;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class DefaultDomainLimiter implements DomainLimiter {
    private String rootDomain = "0";
    private List<DomainExtractor> domainExtractors;
    private TenantExtractor tenantExtractor;

    public void setTenantExtractor(TenantExtractor tenantExtractor) {
        this.tenantExtractor = tenantExtractor;
    }

    public void setDomainExtractors(List<DomainExtractor> domainExtractors) {
        this.domainExtractors = domainExtractors;
    }

    @Override
    public void limitRequestToDomain(HttpServletRequest request) {
        String tenantDomain = tenantExtractor.getDomain(request)
                .orElse(rootDomain);
        for (DomainExtractor extractor : domainExtractors) {
            Optional<String> domain = extractor.extractDomainId(request);
            if (domain.isPresent() && !tenantDomain.equals(rootDomain) && !tenantDomain.equals(domain.get())) {
                throw new BadCredentialsException("invalid.domain");
            }
        }
    }
}
