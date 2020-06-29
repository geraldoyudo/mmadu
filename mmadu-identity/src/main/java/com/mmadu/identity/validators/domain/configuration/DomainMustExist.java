package com.mmadu.identity.validators.domain.configuration;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.services.domain.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class DomainMustExist implements DomainConfigurationValidationStrategy {
    private DomainService domainService;

    @Autowired
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public boolean apply(DomainIdentityConfiguration configuration) {
        return !StringUtils.isEmpty(configuration.getDomainId());
    }

    @Override
    public void validate(DomainIdentityConfiguration configuration, Errors errors) {
        if (domainService.findById(configuration.getDomainId()).isEmpty()) {
            errors.rejectValue("domainId", "domain.not.found", "Domain is not found");
        }
    }
}
