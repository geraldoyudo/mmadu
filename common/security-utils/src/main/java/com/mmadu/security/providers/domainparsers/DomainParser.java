package com.mmadu.security.providers.domainparsers;

import java.util.Optional;

public interface DomainParser {

    Optional<String> parseDomain(Object request);
}
