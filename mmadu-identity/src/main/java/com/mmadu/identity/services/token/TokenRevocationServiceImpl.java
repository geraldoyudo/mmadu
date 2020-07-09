package com.mmadu.identity.services.token;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenNotFoundException;
import com.mmadu.identity.models.Revokable;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRevocationRequest;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenRevocationServiceImpl implements TokenRevocationService {
    private TokenRepository tokenRepository;
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private MmaduClient mmaduClient;

    @Autowired
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void revokeToken(TokenRevocationRequest request) {
        String clientIdentifier = Optional.ofNullable(mmaduClient.getClientIdentifier())
                .orElse(request.getClient_id());
        Token token = tokenRepository.findByClientIdentifierAndTokenString(clientIdentifier, request.getToken())
                .orElseThrow(TokenNotFoundException::new);
        ZonedDateTime now = ZonedDateTime.now();
        if (token.isExpired() || token.isRevoked()
                || (token.getExpiryTime() != null && token.getExpiryTime().isBefore(now))
                || (token.getRevokedTime() != null && token.getRevokedTime().isBefore(now))) {
            return;
        }

        List<Token> tokensToRevoke = new LinkedList<>();
        tokensToRevoke.add(token);
        Optional<GrantAuthorization> authorization = grantAuthorizationRepository.findById(token.getGrantAuthorizationId());
        if (authorization.isPresent()) {
            tokensToRevoke.addAll(tokenRepository.findByGrantAuthorizationId(authorization.get().getId()));
            revoke(Collections.singletonList(authorization.get()), grantAuthorizationRepository);
        }
        revoke(tokensToRevoke, tokenRepository);
    }

    private <T extends Revokable> void revoke(List<T> revokables, CrudRepository<T, String> repository) {
        revokables.forEach(Revokable::revoke);
        repository.saveAll(revokables);
    }
}
