package com.mmadu.otp.service.providers.authorization;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import com.mmadu.otp.service.repositories.DomainOtpConfigurationRepository;
import com.mmadu.security.providers.authorization.DomainJwkSetUriResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(100)
public class DefaultDomainJwkSetUriResolver implements DomainJwkSetUriResolver {
    private DomainOtpConfigurationRepository domainOtpConfigurationRepository;

    @Autowired
    public void setDomainOtpConfigurationRepository(DomainOtpConfigurationRepository domainOtpConfigurationRepository) {
        this.domainOtpConfigurationRepository = domainOtpConfigurationRepository;
    }

    @Override
    public Optional<String> getJwkSetUriForDomain(String domain) {
        return domainOtpConfigurationRepository.findByDomainId(domain)
                .map(DomainOtpConfiguration::getJwkSetUri);
    }
}
