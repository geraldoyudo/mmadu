package com.mmadu.identity.validators.domain;

import com.mmadu.identity.entities.HasDomain;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
public class HasDomainValidator implements Validator {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HasDomain.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        log.trace("Validating HasDomain {}", o);
        HasDomain hasDomain = (HasDomain) o;

        if (hasDomain.getDomainId() != null && domainIdentityConfigurationService.findByDomainId(hasDomain.getDomainId()).isEmpty()) {
            errors.rejectValue("domainId", "domain.not.found", "Domain is not found");
        }
    }
}
