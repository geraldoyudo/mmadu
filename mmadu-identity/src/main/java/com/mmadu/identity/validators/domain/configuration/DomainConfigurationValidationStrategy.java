package com.mmadu.identity.validators.domain.configuration;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import org.springframework.validation.Errors;

public interface DomainConfigurationValidationStrategy {

    boolean apply(DomainIdentityConfiguration configuration);

    void validate(DomainIdentityConfiguration configuration, Errors errors);
}
