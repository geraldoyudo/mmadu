package com.mmadu.identity.validators.client.instancestrategies;

import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.ClientType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PublicClientsShouldHaveRedirectionUrisSet implements ClientInstanceValidatingStrategy {
    @Override
    public boolean apply(ClientInstance instance) {
        return ClientType.PUBLIC.equals(instance.getClientType());
    }

    @Override
    public void validate(ClientInstance instance, Errors errors) {
        if (instance.getRedirectionUris() == null || instance.getRedirectionUris().isEmpty()) {
            errors.rejectValue("redirectionUris", "redirectionUris.required", "redirection uris are required for public clients");
        }
    }
}
