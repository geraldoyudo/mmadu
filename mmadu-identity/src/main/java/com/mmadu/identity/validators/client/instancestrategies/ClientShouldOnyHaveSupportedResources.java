package com.mmadu.identity.validators.client.instancestrategies;

import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.services.resource.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Component
public class ClientShouldOnyHaveSupportedResources implements ClientInstanceValidatingStrategy {
    private ResourceService resourceService;

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public boolean apply(ClientInstance instance) {
        return !StringUtils.isEmpty(instance.getDomainId()) &&
                CollectionUtils.isEmpty(instance.getResources());
    }

    @Override
    public void validate(ClientInstance instance, Errors errors) {
        if (!resourceService.areAllResourcesSupportedInDomain(instance.getDomainId(), instance.getResources()) ||
                !resourceService.supportsTokenCategory(instance.getDomainId(),
                        instance.getResources(), instance.getTokenCategory())) {
            rejectResourceNotSupported(errors);
        }
    }

    private void rejectResourceNotSupported(Errors errors) {
        errors.rejectValue("resources", "resources.not.supported", "some or all of the resources are not supported");
    }
}
