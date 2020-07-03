package com.mmadu.identity.providers.token;

import com.mmadu.identity.entities.Token;
import com.mmadu.identity.entities.token.TokenCredentials;
import com.mmadu.identity.exceptions.TokenCreationException;
import com.mmadu.identity.models.token.TokenCreationResult;
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
                .collect(Collectors.toMap(TokenProvider::providerId, p -> p));
    }

    @Override
    public Token createToken(TokenSpecification spec) {
        TokenCreationResult result = Optional.ofNullable(tokenProviderMap.get(spec.getProvider()))
                .orElseThrow(() -> new TokenCreationException("provider not found"))
                .create(spec);
        TokenSpecification resultantSpec = result.getSpecification();
        TokenCredentials credentials = result.getCredentials();
        Token token = createBaseToken(resultantSpec);
        token.setCredentials(credentials);
        token.setTokenIdentifier(credentials.getTokenIdentifier());
        token.setTokenString(credentials.getTokenString());
        return tokenRepository.save(token);
    }

    private Token createBaseToken(TokenSpecification specification) {
        Token token = new Token();
        token.setDomainId(specification.getDomainId());
        token.setClientId(specification.getGrantAuthorization().getClientId());
        token.setClientInstanceId(specification.getGrantAuthorization().getClientInstanceId());
        token.setClientIdentifier(specification.getGrantAuthorization().getClientIdentifier());
        token.setUserId(specification.getGrantAuthorization().getUserId());
        token.setUsername(specification.getGrantAuthorization().getUsername());
        token.setGrantAuthorizationId(specification.getGrantAuthorization().getId());
        token.setAuthorizationGrantType(specification.getGrantAuthorization().getGrantType());
        token.setLabels(specification.getLabels());
        if(specification.getScopes() == null || specification.getScopes().isEmpty()){
            token.setScopes(specification.getGrantAuthorization().getScopes());
        }else {
            token.setScopes(specification.getScopes());
        }
        token.setType(specification.getType());
        token.setProvider(specification.getProvider());
        token.setExpiryTime(specification.getExpirationTime());
        token.setActivationTime(specification.getActivationTime());
        token.setActive(specification.isActive());
        token.setCategory(specification.getCategory());
        token.setIssuer(specification.getIssuer());
        token.setAudience(specification.getAudience());
        return token;
    }
}
