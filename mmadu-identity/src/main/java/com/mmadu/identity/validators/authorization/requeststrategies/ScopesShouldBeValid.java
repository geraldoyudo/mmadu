package com.mmadu.identity.validators.authorization.requeststrategies;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.services.user.ScopeService;
import com.mmadu.identity.utils.StringListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
@Order(20)
public class ScopesShouldBeValid implements AuthorizationRequestValidationStrategy {
    private MmaduClientService mmaduClientService;
    private ScopeService scopeService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Autowired
    public void setScopeService(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @Override
    public boolean apply(AuthorizationRequest request) {
        return !StringUtils.isEmpty(request.getScope()) && !StringUtils.isEmpty(request.getClient_id());
    }

    @Override
    public void validate(AuthorizationRequest request, Errors errors) {
        Optional<MmaduClient> client = mmaduClientService.loadClientByIdentifier(request.getClient_id());
        if (client.isPresent() &&
                !scopeService.areAllSupportedByDomain(client.get().getDomainId(),
                        StringListUtils.toList(request.getScope()))) {
            errors.rejectValue("scope", "scope.not.supported", "one or more of the scopes are invalid");
        }
    }
}
