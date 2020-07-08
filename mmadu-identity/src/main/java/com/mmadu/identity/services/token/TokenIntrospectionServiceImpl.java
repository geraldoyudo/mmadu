package com.mmadu.identity.services.token;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenNotFoundException;
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
        Token token = tokenRepository.findByTokenString(request.getToken())
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
