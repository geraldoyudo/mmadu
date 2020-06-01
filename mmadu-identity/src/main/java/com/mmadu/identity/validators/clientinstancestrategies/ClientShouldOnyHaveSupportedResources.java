package com.mmadu.identity.validators.clientinstancestrategies;

import com.mmadu.identity.entities.ClientInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class ClientShouldOnyHaveSupportedResources implements ClientInstanceValidatingStrategy {
    @Override
    public boolean apply(ClientInstance instance) {
        return !StringUtils.isEmpty(instance.getDomainId()) &&
                CollectionUtils.isEmpty(instance.getResources());
    }

    @Override
    public void validate(ClientInstance instance, Errors errors) {

    }
}
