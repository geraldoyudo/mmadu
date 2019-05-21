package com.mmadu.service.security.domainidextractors;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TransparentDomainIdExtractor implements DomainIdExtractor{

    @Override
    public Optional<String> extractDomainId(Object permissionObject) {
        return Optional.ofNullable((String) permissionObject);
    }

    @Override
    public String domain() {
        return "domain";
    }
}
