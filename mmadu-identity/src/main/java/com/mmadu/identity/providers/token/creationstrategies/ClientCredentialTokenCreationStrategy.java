package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.ClientCredentialsGrantData;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.repositories.TokenRepository;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.min;

@Component
public class ClientCredentialTokenCreationStrategy implements TokenCreationStrategy {
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private TokenRepository tokenRepository;
    private TokenFactory tokenFactory;

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setTokenFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public boolean apply(TokenRequest request, TokenContext context) {
        return GrantTypeUtils.CLIENT_CREDENTIALS.equals(request.getGrant_type()) &&
                Optional.ofNullable(context.getClient().getSupportedGrantTypes())
                        .orElse(emptyList()).contains(GrantTypeUtils.CLIENT_CREDENTIALS);
    }

    @Override
    public TokenResponse getToken(TokenRequest request, TokenContext context) {
        MmaduClient client = context.getClient();
        DomainIdentityConfiguration configuration = context.getConfiguration();
        ZonedDateTime now = ZonedDateTime.now();

        Optional<GrantAuthorization> authorizationOptional = grantAuthorizationRepository.findByClientIdentifierAndGrantTypeAndActive(
                client.getClientIdentifier(), GrantTypeUtils.CLIENT_CREDENTIALS, true
        ).stream().filter(authorization -> authorization.getExpiryTime().isAfter(now))
                .findFirst();

        if (authorizationOptional.isPresent()) {
            GrantAuthorization grantAuthorization = authorizationOptional.get();
            List<String> tokens = grantAuthorization.getAccessTokens();
            if (!tokens.isEmpty()) {
                Optional<Token> accessToken = tokenRepository.findById(tokens.get(0));
                if (accessToken.isPresent() && accessToken.get().getExpiryTime().isAfter(now)) {
                    Token token = accessToken.get();
                    return TokenResponse
                            .builder()
                            .expiresIn(token.getExpiryTime())
                            .tokenType("bearer")
                            .tokenIdentifier(token.getTokenIdentifier())
                            .accessToken(token.getTokenString())
                            .build();
                }
            }
        }
        authorizationOptional.ifPresent(auth -> revokeAuthentication(now, auth));
        GrantAuthorization grantAuthorization = createClientCredentialsAuthorization(client, configuration, now);
        Token accessToken = tokenFactory.createToken(
                TokenSpecification.builder()
                        .configuration(configuration.getAccessTokenProperties())
                        .domainId(client.getDomainId())
                        .grantAuthorization(grantAuthorization)
                        .activationTime(now)
                        .issueTime(now)
                        .expirationTime(grantAuthorization.getExpiryTime())
                        .labels(List.of("access_token"))
                        .provider(configuration.getAccessTokenProvider())
                        .type("access_token")
                        .category(client.getTokenCategory())
                        .active(true)
                        .build()
        );
        grantAuthorization.addAccessToken(accessToken);
        grantAuthorizationRepository.save(grantAuthorization);
        return TokenResponse.builder()
                .accessToken(accessToken.getTokenString())
                .tokenIdentifier(accessToken.getTokenIdentifier())
                .tokenType(accessToken.getCategory())
                .expiresIn(grantAuthorization.getExpiryTime())
                .build();
    }

    private void revokeAuthentication(ZonedDateTime now, GrantAuthorization auth) {
        auth.setActive(false);
        auth.setRevoked(true);
        auth.setRevokedTime(now);
        grantAuthorizationRepository.save(auth);
    }

    private GrantAuthorization createClientCredentialsAuthorization(MmaduClient client, DomainIdentityConfiguration configuration, ZonedDateTime now) {
        GrantAuthorization grantAuthorization = new GrantAuthorization();
        grantAuthorization.setClientInstanceId(client.getInstanceId());
        grantAuthorization.setClientId(client.getId());
        grantAuthorization.setDomainId(client.getDomainId());
        grantAuthorization.setClientIdentifier(client.getClientIdentifier());
        grantAuthorization.setGrantType(GrantTypeUtils.CLIENT_CREDENTIALS);
        grantAuthorization.setIssuedTime(now);
        grantAuthorization.setExpiryTime(
                now.plusSeconds(min(configuration.getMaxAuthorizationTTLSeconds(), client.getClientCredentialsGrantTypeTTLSeconds()))
        );
        grantAuthorization.setData(new ClientCredentialsGrantData());
        grantAuthorization.setActive(true);
        grantAuthorization = grantAuthorizationRepository.save(grantAuthorization);
        return grantAuthorization;
    }
}
