package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.entities.ClientType;
import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import java.util.Optional;

@Component
public class RequireClientIdForPublicClients implements TokenRequestValidationStrategy {
    private MmaduClient mmaduClient;
    private MmaduClientService mmaduClientService;
    private ClientTokenRequestAuthenticator clientTokenRequestAuthenticator;

    @Autowired(required = false)
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Autowired
    public void setClientTokenRequestAuthenticator(ClientTokenRequestAuthenticator clientTokenRequestAuthenticator) {
        this.clientTokenRequestAuthenticator = clientTokenRequestAuthenticator;
    }

    @Override
    public boolean apply(TokenRequest request) {
        return mmaduClient == null;
    }

    @Override
    public void validate(TokenRequest request, Errors errors) {
        if (StringUtils.isEmpty(request.getClientId())) {
            errors.rejectValue("client_id", "client_id.required", "client id is required");
        } else {
            Optional<MmaduClient> clientOptional = mmaduClientService.loadClientByIdentifier(request.getClientId());
            if (clientOptional.isEmpty()) {
                errors.rejectValue("client_id", "client.not.found", "client not found");
            } else {
                MmaduClient client = clientOptional.get();
                if (ClientType.CONFIDENTIAL.equals(client.getClientType())) {
                    errors.rejectValue("client_id", "client.not.authorized", "client not authorized");
                } else {
                    try {
                        clientTokenRequestAuthenticator.authenticatePublicClient(request, client);
                    } catch (AuthenticationException ex) {
                        errors.rejectValue("client_id", "client.authentication.failed", "client authentication failed");
                    }
                }
            }
        }
    }
}
