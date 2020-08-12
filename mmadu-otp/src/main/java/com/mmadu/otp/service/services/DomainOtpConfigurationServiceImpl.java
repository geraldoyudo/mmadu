package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import com.mmadu.otp.service.repositories.DomainOtpConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DomainOtpConfigurationServiceImpl implements DomainOtpConfigurationService {
    private DomainOtpConfigurationRepository domainOtpConfigurationRepository;

    @Autowired
    public void setDomainOtpConfigurationRepository(DomainOtpConfigurationRepository domainOtpConfigurationRepository) {
        this.domainOtpConfigurationRepository = domainOtpConfigurationRepository;
    }

    @Override
    @Cacheable("domainOtpConfigurations")
    public Optional<DomainOtpConfiguration> findByDomainId(String domainId) {
        return domainOtpConfigurationRepository.findById(domainId);
    }
}
