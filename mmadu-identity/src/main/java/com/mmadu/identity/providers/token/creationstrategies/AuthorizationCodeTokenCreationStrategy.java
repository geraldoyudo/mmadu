package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.AuthorizationCodeGrantData;
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
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.utils.GrantTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class AuthorizationCodeTokenCreationStrategy implements TokenCreationStrategy {
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private TokenFactory tokenFactory;
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

    @Autowired(required = false)
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setTokenFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    public boolean apply(TokenRequest request, MmaduClient client) {
        return GrantTypeUtils.AUTHORIZATION_CODE.equals(request.getGrant_type());
    }

    @Override
    public TokenResponse getToken(TokenRequest request, MmaduClient client) {
        GrantAuthorization authorization = grantAuthorizationRepository.findByAuthorizationCode(request.getCode())
                .orElseThrow(AuthorizationCodeTokenCreationStrategy::invalidCodeError);

        if (!(authorization.getData() instanceof AuthorizationCodeGrantData)) {
            throw invalidGrantData();
        }

        if (authorization.isRedirectUriSpecified() && StringUtils.isEmpty(request.getRedirect_uri())) {
            throw redirectUriIsRequired();
        }

        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(client.getDomainId())
                .orElseThrow(AuthorizationCodeTokenCreationStrategy::invalidClient);

        Token accessToken = tokenFactory.createToken(
                TokenSpecification.builder()
                        .configuration(configuration.getAccessTokenProperties())
                        .domainId(client.getDomainId())
                        .grantAuthorization(authorization)
                        .labels(List.of("access_token"))
                        .provider(configuration.getAccessTokenProvider())
                        .type("access_token")
                        .build()
        );
        String refreshTokenString = "";

        if (configuration.isRefreshTokenEnabled() && client.issueRefreshTokens()) {
            Token refreshToken = tokenFactory.createToken(
                    TokenSpecification.builder()
                            .configuration(configuration.getAccessTokenProperties())
                            .domainId(client.getDomainId())
                            .grantAuthorization(authorization)
                            .labels(List.of("access_token"))
                            .provider(configuration.getAccessTokenProvider())
                            .type("access_token")
                            .build()
            );

            refreshTokenString = refreshToken.getCredentials().toString();
        }

        return TokenResponse.builder()
                .accessToken(accessToken.getCredentials().toString())
                .expiresIn(authorization.getExpiryTime())
                .refreshToken(refreshTokenString)
                .tokenIdentifier(accessToken.getTokenIdentifier())
                .build();
    }

    private static TokenException invalidCodeError() {
        return new TokenException(new InvalidRequest("code.invalid", ""));
    }

    private static TokenException redirectUriIsRequired() {
        return new TokenException(new InvalidRequest("redirect_uri.required", ""));
    }

    private static TokenException invalidGrantData() {
        return new TokenException(new InvalidRequest("grant_data.invalid", ""));
    }

    private static TokenException invalidClient() {
        return new TokenException(new InvalidClient("client.domain.invalid", ""));
    }
}
