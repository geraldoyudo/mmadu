package com.mmadu.identity.providers.token;

import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.InvalidGrant;
import com.mmadu.identity.providers.token.creationstrategies.TokenCreationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TokenCreationProviderImpl implements TokenCreationProvider {
    private List<TokenCreationStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<TokenCreationStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public TokenResponse createToken(TokenRequest request, TokenContext context) {
        Optional<TokenCreationStrategy> strategyOptional = strategies.stream()
                .filter(s -> s.apply(request, context))
                .findFirst();
        if (strategyOptional.isEmpty()) {
            throw new TokenException(new InvalidGrant("cannot create token", ""));
        }
        return strategyOptional.get().getToken(request, context);
    }
}
