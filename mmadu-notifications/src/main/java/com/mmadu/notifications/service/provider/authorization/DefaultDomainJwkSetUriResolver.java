package com.mmadu.notifications.service.provider.authorization;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import com.mmadu.notifications.service.repositories.DomainNotificationConfigurationRepository;
import com.mmadu.security.providers.authorization.DomainJwkSetUriResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(100)
public class DefaultDomainJwkSetUriResolver implements DomainJwkSetUriResolver {
    private DomainNotificationConfigurationRepository domainNotificationConfigurationRepository;

    @Autowired
    public void setDomainNotificationConfigurationRepository(DomainNotificationConfigurationRepository domainNotificationConfigurationRepository) {
        this.domainNotificationConfigurationRepository = domainNotificationConfigurationRepository;
    }

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return domainNotificationConfigurationRepository.findByDomainId(domain)
                .map(DomainNotificationConfiguration::getJwkSetUri);
    }
}
