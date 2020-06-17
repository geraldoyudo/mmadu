package com.mmadu.security.providers.domainparsers;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class HttpHeaderDomainExtractor implements HttpRequestDomainExtractor {
    private List<String> domainHeaderNames = List.of("domainId", "domain");

    public void setDomainHeaderNames(List<String> domainHeaderNames) {
        this.domainHeaderNames = domainHeaderNames;
    }

    @Override
    public Optional<String> extractDomainIdFromRequest(HttpServletRequest request) {
        return domainHeaderNames.stream()
                .flatMap(domainHeader -> Optional.ofNullable(request.getHeader(domainHeader)).stream())
                .findFirst();
    }
}
