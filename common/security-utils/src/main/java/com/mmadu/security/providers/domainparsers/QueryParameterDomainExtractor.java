package com.mmadu.security.providers.domainparsers;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class QueryParameterDomainExtractor implements HttpRequestDomainExtractor {
    private List<String> domainParameterNames = List.of("domainId", "domain");

    public void setDomainParameterNames(List<String> domainParameterNames) {
        this.domainParameterNames = domainParameterNames;
    }

    @Override
    public Optional<String> extractDomainIdFromRequest(HttpServletRequest request) {
        return domainParameterNames.stream()
                .flatMap(domainHeader -> Optional.ofNullable(request.getParameter(domainHeader)).stream())
                .findFirst();
    }
}
