package com.mmadu.identity.security.clients;

import com.mmadu.identity.entities.ClientType;
import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.models.client.MmaduClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClientRequestAuthenticatorImpl implements ClientRequestAuthenticator {
    private HttpServletRequest httpRequest;
    private List<ClientCredentialRequestAuthenticationStrategy> strategies = Collections.emptyList();

    @Autowired
    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Autowired(required = false)
    public void setStrategies(List<ClientCredentialRequestAuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void authenticateClient(ClientCredentialRequestPayload payload, MmaduClient client) {
        Optional<ClientCredentialRequestAuthenticationStrategy> strategy = strategies.stream()
                .filter(s -> s.apply(httpRequest, payload, client)).findFirst();
        if (strategy.isEmpty() && ClientType.CONFIDENTIAL.equals(client.getClientType())) {
            throw new AuthenticationException("confidential client could not be authenticated");
        }
        if (strategy.isPresent() && !strategy.get().isPermitted(httpRequest, payload, client)) {
            throw new AuthenticationException("client denied");
        }
    }
}
