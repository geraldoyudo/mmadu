package com.mmadu.security.providers.domainparsers;

import com.mmadu.security.api.DomainPayload;

import java.util.Optional;

public class DomainPayloadExtractor implements DomainExtractor {
    @Override
    public Optional<String> extractDomainId(Object request) {
        if (request instanceof DomainPayload) {
            return Optional.ofNullable(((DomainPayload) request).getDomainId());
        } else {
            return Optional.empty();
        }
    }
}
