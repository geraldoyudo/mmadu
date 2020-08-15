package com.mmadu.service.providers.authorization;

import com.mmadu.security.providers.authorization.DomainJwkSetUriResolver;
import com.mmadu.service.entities.AppDomain;
import com.mmadu.service.repositories.AppDomainRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Order(100)
public class DefaultDomainJwkSetUriResolver implements DomainJwkSetUriResolver {
    private AppDomainRepository appDomainRepository;

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return appDomainRepository.findById(domain)
                .map(AppDomain::getJwkSetUri)
                .filter(Objects::nonNull);
    }
}
