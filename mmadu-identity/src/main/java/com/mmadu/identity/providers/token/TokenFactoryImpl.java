package com.mmadu.identity.providers.token;

import com.mmadu.identity.entities.Token;
import com.mmadu.identity.entities.token.TokenCredentials;
import com.mmadu.identity.exceptions.TokenCreationException;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.providers.token.providers.TokenProvider;
import com.mmadu.identity.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TokenFactoryImpl implements TokenFactory {
    private Map<String, TokenProvider> tokenProviderMap = Collections.emptyMap();
    private TokenRepository tokenRepository;

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setTokenProviders(List<TokenProvider> tokenProviders) {
        tokenProviderMap = tokenProviders.stream()
                .collect(Collectors.toMap(TokenProvider::type, p -> p));
    }

    @Override
    public Token createToken(TokenSpecification spec) {
        TokenCredentials credentials = Optional.ofNullable(tokenProviderMap.get(spec.getType()))
                .orElseThrow(() -> new TokenCreationException("provider not found"))
                .create(spec);
        Token token = createBaseToken(spec);
        token.setCredentials(credentials);
        return tokenRepository.save(token);
    }

    private Token createBaseToken(TokenSpecification specification) {
        Token token = new Token();
        token.setDomainId(specification.getDomainId());
        token.setClientId(specification.getClient().getId());
        token.setClientInstanceId(specification.getClient().getId());
        token.setUserId(specification.getUserId());
        token.setGrantAuthorizationId(specification.getGrantAuthorization().getId());
        token.setLabels(specification.getLabels());
        token.setScopes(specification.getScopes());
        token.setType(specification.getType());
        return token;
    }
}
