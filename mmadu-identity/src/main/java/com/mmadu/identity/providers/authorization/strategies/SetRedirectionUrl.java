package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Component
@Order(20)
public class SetRedirectionUrl implements AuthorizationStrategy {
    private MmaduClientService clientService;

    @Autowired
    public void setClientService(MmaduClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return true;
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        Optional<MmaduClient> clientOptional = clientService.loadClientByIdentifier(request.getClient_id());
        if (clientOptional.isEmpty()) {
            throw new AuthorizationException("client not found");
        }
        String redirectUri = request.getRedirect_uri();
        List<String> clientRedirectUris = clientOptional.get().getRedirectUris();
        if (StringUtils.isEmpty(redirectUri)) {
            if (clientRedirectUris.size() != 1) {
                throw new AuthorizationException("multiple redirect uris are configured, ensure redirect_uri key is present");
            }
            context.setRedirectUri(clientRedirectUris.get(0), false);
        } else {
            if (clientRedirectUris.isEmpty() || clientRedirectUris.contains(redirectUri)) {
                context.setRedirectUri(redirectUri, true);
            } else {
                throw new AuthorizationException("invalid redirect uri");
            }
        }
    }
}
