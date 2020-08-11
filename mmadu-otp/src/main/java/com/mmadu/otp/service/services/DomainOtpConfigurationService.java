package com.mmadu.otp.service.services;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;

import java.util.Optional;

public interface DomainOtpConfigurationService {

    Optional<DomainOtpConfiguration> findByDomainId(String domainId);
}
