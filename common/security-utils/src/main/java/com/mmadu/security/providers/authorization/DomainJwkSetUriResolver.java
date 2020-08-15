package com.mmadu.security.providers.authorization;

import java.util.Optional;

public interface DomainJwkSetUriResolver {

    Optional<String> getJwkSetUriForDomain(String domain);
}
