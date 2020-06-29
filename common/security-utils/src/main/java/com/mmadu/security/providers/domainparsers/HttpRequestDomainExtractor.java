package com.mmadu.security.providers.domainparsers;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface HttpRequestDomainExtractor extends DomainExtractor {

    @Override
    default Optional<String> extractDomainId(Object request) {
        if (request instanceof HttpServletRequest) {
            return extractDomainIdFromRequest((HttpServletRequest) request);
        } else {
            return Optional.empty();
        }
    }

    Optional<String> extractDomainIdFromRequest(HttpServletRequest request);
}
