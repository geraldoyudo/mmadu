package com.mmadu.identity.services.token;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenNotFoundException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenIntrospectionRequest;
import com.mmadu.identity.models.token.TokenIntrospectionResponse;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class TokenIntrospectionServiceImpl implements TokenIntrospectionService {
    private TokenRepository tokenRepository;
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private MmaduClient mmaduClient;

    @Autowired
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Override
    public TokenIntrospectionResponse getTokenDetails(TokenIntrospectionRequest request) {
        String clientIdentifier = Optional.ofNullable(mmaduClient.getClientIdentifier())
                .orElse(request.getClient_id());
        Token token = tokenRepository.findByClientIdentifierAndTokenString(clientIdentifier, request.getToken())
                .orElseThrow(TokenNotFoundException::new);
        ZonedDateTime now = ZonedDateTime.now();
        if (token.isExpired() || token.isRevoked()
                || (token.getExpiryTime() != null && token.getExpiryTime().isBefore(now))
                || (token.getRevokedTime() != null && token.getRevokedTime().isBefore(now))) {
            return TokenIntrospectionResponse.inactiveToken();
        } else {
            Optional<GrantAuthorization> authorization = grantAuthorizationRepository.findById(token.getGrantAuthorizationId());
            return TokenIntrospectionResponse.fromTokenAndAuthorization(token, authorization);
        }
    }
}
