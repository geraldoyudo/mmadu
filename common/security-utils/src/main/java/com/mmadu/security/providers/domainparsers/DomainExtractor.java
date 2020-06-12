package com.mmadu.security.providers.domainparsers;

import java.util.Optional;

public interface DomainExtractor {
    Optional<String> extractDomainId(Object request);
}
