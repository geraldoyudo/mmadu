package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.entities.ClientType;
import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.validators.token.strategies.authentication.ClientTokenRequestAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ClientTokenRequestAuthenticatorImpl implements ClientTokenRequestAuthenticator {
    private List<ClientTokenRequestAuthenticationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<ClientTokenRequestAuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void authenticatePublicClient(TokenRequest request, MmaduClient client) {
        Optional<ClientTokenRequestAuthenticationStrategy> strategy = strategies.stream()
                .filter(s -> s.apply(request, client)).findFirst();
        if (strategy.isEmpty() && ClientType.CONFIDENTIAL.equals(client.getClientType())) {
            throw new AuthenticationException("confidential client could not be authenticated");
        }
        if (strategy.isPresent() && !strategy.get().isPermitted(request, client)) {
            throw new AuthenticationException("client denied");
        }
    }
}
