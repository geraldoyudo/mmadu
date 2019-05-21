package com.mmadu.service.security.domainidextractors;

import java.util.Optional;

public interface DomainIdExtractor {

    Optional<String> extractDomainId(Object permissionObject);

    String domain();
}
