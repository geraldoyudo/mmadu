package com.mmadu.identity.validators.clientinstancestrategies;

import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.ClientType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ConfidentialClientsShouldHaveCredentials implements ClientInstanceValidatingStrategy {
    @Override
    public boolean apply(ClientInstance instance) {
        return ClientType.CONFIDENTIAL.equals(instance.getClientType());
    }

    @Override
    public void validate(ClientInstance instance, Errors errors) {
        if(instance.getCredentials() == null){
            errors.rejectValue("credentials", "credentials.required", "confidential clients should have credentials set");
        }
    }
}
