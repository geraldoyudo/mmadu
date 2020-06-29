package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.models.token.error.InvalidClient;
import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.models.token.error.InvalidScope;
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.repositories.TokenRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.utils.GrantTypeUtils;
import com.mmadu.identity.utils.StringListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static com.mmadu.identity.utils.ZoneDateTimeUtils.min;

@Component
public class RefreshTokenCreationStrategy implements TokenCreationStrategy {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private GrantAuthorizationRepository grantAuthorizationRepository;
    @Autowired
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    @Autowired
    private TokenFactory tokenFactory;

    @Override
    public boolean apply(TokenRequest request, MmaduClient client) {
        return GrantTypeUtils.REFRESH_TOKEN.equals(request.getGrant_type());
    }

    @Override
    public TokenResponse getToken(TokenRequest request, MmaduClient client) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(client.getDomainId())
                .orElseThrow(RefreshTokenCreationStrategy::invalidClient);

        Token refreshToken = tokenRepository.findByTokenString(request.getRefresh_token())
                .orElseThrow(RefreshTokenCreationStrategy::invalidRefreshToken);
        if (ZonedDateTime.now().isAfter(refreshToken.getExpiryTime()) ||
                !refreshToken.isActive() || refreshToken.isRevoked()) {
            throw invalidRefreshToken();
        }
        GrantAuthorization authorization = grantAuthorizationRepository.findById(refreshToken.getGrantAuthorizationId())
                .orElseThrow(RefreshTokenCreationStrategy::invalidRefreshToken);
        if (ZonedDateTime.now().isAfter(authorization.getExpiryTime()) ||
                !authorization.isActive() || authorization.isRevoked()) {
            throw invalidRefreshToken();
        }

        List<Token> tokens = tokenRepository.findByGrantAuthorizationIdAndTypeAndActive(authorization.getId(),
                "access_token", true);
        if (authorization.isRefreshTokenIssued()) {
            tokens.addAll(tokenRepository.findByGrantAuthorizationIdAndTypeAndActive(authorization.getId(),
                    "refresh_token", true));
        }
        List<String> scopes = Collections.emptyList();
        if (!StringUtils.isEmpty(request.getScope())) {
            scopes = StringListUtils.toList(request.getScope());
        }
        if (!authorization.getScopes().containsAll(scopes)) {
            throw invalidScope();
        }
        revokeTokens(tokens);
        ZonedDateTime now = ZonedDateTime.now();
        Token newAccessToken = tokenFactory.createToken(
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
                        .scopes(scopes)
                        .active(true)
                        .build()
        );
        authorization.addAccessToken(newAccessToken);
        String refreshTokenString = "";

        if (configuration.isRefreshTokenEnabled() &&
                client.issueRefreshTokens()
                && authorization.isRefreshTokenIssued()) {
            Token newRefreshToken = tokenFactory.createToken(
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

            refreshTokenString = newRefreshToken.getCredentials().getTokenString();
            authorization.addRefreshToken(newRefreshToken);
        }
        grantAuthorizationRepository.save(authorization);
        return TokenResponse.builder()
                .accessToken(newAccessToken.getCredentials().getTokenString())
                .expiresIn(authorization.getExpiryTime())
                .refreshToken(refreshTokenString)
                .tokenIdentifier(newAccessToken.getTokenIdentifier())
                .tokenType(newAccessToken.getCategory())
                .expiresIn(newAccessToken.getExpiryTime())
                .build();
    }

    private void revokeTokens(List<Token> tokens) {
        ZonedDateTime now = ZonedDateTime.now();
        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setRevokedTime(now);
            token.setActive(false);
        });
        tokenRepository.saveAll(tokens);
    }

    private static TokenException invalidRefreshToken() {
        return new TokenException(new InvalidRequest("request_token.invalid", ""));
    }

    private static TokenException invalidClient() {
        return new TokenException(new InvalidClient("client.domain.invalid", ""));
    }

    private static TokenException invalidScope() {
        return new TokenException(new InvalidScope("scope.invalid", ""));
    }
}
