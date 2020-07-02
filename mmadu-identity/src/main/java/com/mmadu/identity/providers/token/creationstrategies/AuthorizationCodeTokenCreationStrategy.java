package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.AuthorizationCodeGrantData;
import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.models.token.error.InvalidClient;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.utils.GrantTypeUtils;
import com.mmadu.identity.utils.TokenErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.mmadu.identity.utils.TokenErrorUtils.*;
import static com.mmadu.identity.utils.ZoneDateTimeUtils.min;
import static java.util.Collections.emptyList;

@Component
public class AuthorizationCodeTokenCreationStrategy implements TokenCreationStrategy {
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private TokenFactory tokenFactory;

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setTokenFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public boolean apply(TokenRequest request, TokenContext context) {
        return GrantTypeUtils.AUTHORIZATION_CODE.equals(request.getGrant_type()) &&
                Optional.ofNullable(context.getClient().getSupportedGrantTypes())
                        .orElse(emptyList()).contains(GrantTypeUtils.AUTHORIZATION_CODE);
    }

    @Override
    public TokenResponse getToken(TokenRequest request, TokenContext context) {
        MmaduClient client = context.getClient();
        GrantAuthorization authorization = grantAuthorizationRepository.
                findByClientIdentifierAndAuthorizationCode(client.getClientIdentifier(), request.getCode())
                .orElseThrow(TokenErrorUtils::invalidCodeError);

        if (authorization.isActive() || authorization.isExpired() || authorization.isRevoked()) {
            throw invalidRequest();
        }
        if (!(authorization.getData() instanceof AuthorizationCodeGrantData)) {
            throw invalidGrantData();
        }

        if (authorization.isRedirectUriSpecified() && StringUtils.isEmpty(request.getRedirect_uri())) {
            throw redirectUriIsRequired();
        }

        DomainIdentityConfiguration configuration = context.getConfiguration();

        ZonedDateTime now = ZonedDateTime.now();
        Token accessToken = tokenFactory.createToken(
                TokenSpecification.builder()
                        .configuration(configuration.getAccessTokenProperties())
                        .domainId(client.getDomainId())
                        .grantAuthorization(authorization)
                        .activationTime(now)
                        .issueTime(now)
                        .expirationTime(
                                min(now.plusSeconds(client.getAccessTokenTTLSeconds()), authorization.getExpiryTime())
                        )
                        .labels(List.of("access_token"))
                        .provider(configuration.getAccessTokenProvider())
                        .type("access_token")
                        .category(client.getTokenCategory())
                        .active(true)
                        .build()
        );
        authorization.addAccessToken(accessToken);
        String refreshTokenString = "";

        if (configuration.isRefreshTokenEnabled() && client.issueRefreshTokens()) {
            Token refreshToken = tokenFactory.createToken(
                    TokenSpecification.builder()
                            .configuration(configuration.getRefreshTokenProperties())
                            .domainId(client.getDomainId())
                            .grantAuthorization(authorization)
                            .labels(List.of("refresh_token"))
                            .provider(configuration.getRefreshTokenProvider())
                            .activationTime(now)
                            .issueTime(now)
                            .expirationTime(
                                    min(now.plusSeconds(client.getRefreshTokenTTLSeconds()), authorization.getExpiryTime())
                            )
                            .type("refresh_token")
                            .category(client.getTokenCategory())
                            .active(true)
                            .build()
            );
            refreshTokenString = refreshToken.getCredentials().getTokenString();
            authorization.addRefreshToken(refreshToken);
            authorization.setRefreshTokenIssued(true);
        }
        authorization.setActive(true);
        grantAuthorizationRepository.save(authorization);
        return TokenResponse.builder()
                .accessToken(accessToken.getCredentials().getTokenString())
                .expiresIn(authorization.getExpiryTime())
                .refreshToken(refreshTokenString)
                .tokenIdentifier(accessToken.getTokenIdentifier())
                .tokenType(accessToken.getCategory())
                .expiresIn(accessToken.getExpiryTime())
                .build();
    }
}
