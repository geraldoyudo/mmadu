package com.mmadu.security.providers.authorization;

import java.util.Optional;

public class DefaultJwkSetUriResolver implements DomainJwkSetUriResolver {
    private String defaultJwkSetUri;

    public DefaultJwkSetUriResolver(String defaultJwkSetUri) {
        this.defaultJwkSetUri = defaultJwkSetUri;
    }

    public void setDefaultJwkSetUri(String defaultJwkSetUri) {
        this.defaultJwkSetUri = defaultJwkSetUri;
    }

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return Optional.ofNullable(defaultJwkSetUri);
    }
}
