package com.mmadu.identity.validators.authorization.requeststrategies;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Component
@Order(10)
public class ClientInstanceShouldExist implements AuthorizationRequestValidationStrategy {
    private MmaduClientService mmaduClientService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public boolean apply(AuthorizationRequest request) {
        return !StringUtils.isEmpty(request.getClient_id());
    }

    @Override
    public void validate(AuthorizationRequest request, Errors errors) {
        Optional<MmaduClient> client = mmaduClientService.loadClientByIdentifier(request.getClient_id());
        if (client.isEmpty()) {
            errors.rejectValue("client_id", "client.not.found", "client is not found");
        }

    }
}
