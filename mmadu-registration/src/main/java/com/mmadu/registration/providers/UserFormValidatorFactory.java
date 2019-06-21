package com.mmadu.registration.providers;

import com.mmadu.registration.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFormValidatorFactory {
    @Autowired
    private FieldRepository fieldRepository;

    public UserFormValidator createValidatorForDomain(String domainId) {
        return new UserFormValidator(domainId, fieldRepository.findByDomainId(domainId));
    }
}
