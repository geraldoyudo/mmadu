package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.models.token.error.UnauthorizedClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.services.authorization.ApprovedScopeService;
import com.mmadu.identity.services.authorization.ProposedScopeLimitService;
import com.mmadu.identity.services.user.MmaduUserService;
import com.mmadu.identity.utils.GrantTypeUtils;
import com.mmadu.identity.utils.StringListUtils;
import com.mmadu.identity.utils.TokenErrorUtils;
import com.mmadu.identity.utils.ZoneDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.mmadu.identity.entities.NoGrantData.noGrantData;
import static org.apache.commons.lang3.ObjectUtils.min;

@Component
public class ResourceOwnerCredentialsTokenCreationStrategy implements TokenCreationStrategy {
    private MmaduUserService mmaduUserService;
    private GrantAuthorizationRepository grantAuthorizationRepository;
    private TokenFactory tokenFactory;
    private ProposedScopeLimitService proposedScopeLimitService;
    private ApprovedScopeService approvedScopeService;

    @Autowired
    public void setGrantAuthorizationRepository(GrantAuthorizationRepository grantAuthorizationRepository) {
        this.grantAuthorizationRepository = grantAuthorizationRepository;
    }

    @Autowired
    public void setTokenFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Autowired
    public void setProposedScopeLimitService(ProposedScopeLimitService proposedScopeLimitService) {
        this.proposedScopeLimitService = proposedScopeLimitService;
    }

    @Autowired
    public void setApprovedScopeService(ApprovedScopeService approvedScopeService) {
        this.approvedScopeService = approvedScopeService;
    }

    @Autowired
    public void setMmaduUserService(MmaduUserService mmaduUserService) {
        this.mmaduUserService = mmaduUserService;
    }

    @Override
    public boolean apply(TokenRequest request, TokenContext context) {
        return GrantTypeUtils.AUTHORIZATION_CODE.equals(request.getGrant_type());
    }

    @Override
    public TokenResponse getToken(TokenRequest request, TokenContext context) {
        MmaduClient client = context.getClient();
        DomainIdentityConfiguration configuration = context.getConfiguration();
        authenticateOwnerCredentials(request, client);
        GrantAuthorization authorization = createAuthorization(request, configuration, client);
        Token accessToken = createAccessToken(authorization, client, configuration);
        Optional<Token> refreshToken = createRefreshTokenIfEnabled(authorization, client, configuration);
        authorization.setActive(true);
        grantAuthorizationRepository.save(authorization);
        TokenResponse.TokenResponseBuilder builder = TokenResponse.builder();
        builder.accessToken(accessToken.getTokenString())
                .expiresIn(accessToken.getExpiryTime())
                .tokenIdentifier(accessToken.getTokenIdentifier())
                .tokenType(accessToken.getCategory());
        refreshToken.ifPresent(token -> builder.refreshToken(token.getTokenString()));
        return builder.build();
    }

    private void authenticateOwnerCredentials(TokenRequest request, MmaduClient client) {
        try {
            mmaduUserService.authenticate(client.getDomainId(), request.getUsername(), request.getPassword());
        } catch (AuthenticationException ex) {
            throw unauthorizedClient();
        }
    }

    private GrantAuthorization createAuthorization(TokenRequest request, DomainIdentityConfiguration configuration, MmaduClient client) {
        MmaduUser authorizer = mmaduUserService.loadUserByUsernameAndDomainId(request.getUsername(), client.getDomainId())
                .orElseThrow(TokenErrorUtils::invalidClient);
        List<String> limitedScopes = proposedScopeLimitService.limitScopesForUser(StringListUtils.toList(request.getScope()), authorizer, client);
        List<String> approvedScopes = approvedScopeService.processScopesForUser(limitedScopes, authorizer, client);
        GrantAuthorization grantAuthorization = new GrantAuthorization();
        grantAuthorization.setClientInstanceId(client.getInstanceId());
        grantAuthorization.setClientId(client.getId());
        grantAuthorization.setDomainId(authorizer.getDomainId());
        grantAuthorization.setUserId(authorizer.getId());
        grantAuthorization.setUsername(authorizer.getUsername());
        grantAuthorization.setUserRoles(authorizer.getRoles());
        grantAuthorization.setUserGroups(authorizer.getGroups());
        grantAuthorization.setUserAuthorities(authorizer.getAuthorities());
        grantAuthorization.setScopes(approvedScopes);
        grantAuthorization.setClientIdentifier(client.getClientIdentifier());
        grantAuthorization.setGrantType(GrantTypeUtils.PASSWORD);
        grantAuthorization.setData(noGrantData());
        ZonedDateTime now = ZonedDateTime.now();
        grantAuthorization.setIssuedTime(now);
        grantAuthorization.setExpiryTime(
                now.plusSeconds(min(configuration.getMaxAuthorizationTTLSeconds(), client.getPasswordGrantTypeTTLSeconds()))
        );
        return grantAuthorizationRepository.save(grantAuthorization);
    }

    private Token createAccessToken(GrantAuthorization authorization, MmaduClient client, DomainIdentityConfiguration configuration) {
        ZonedDateTime now = ZonedDateTime.now();
        Token accessToken = tokenFactory.createToken(
                TokenSpecification.builder()
                        .configuration(configuration.getAccessTokenProperties())
                        .domainId(client.getDomainId())
                        .grantAuthorization(authorization)
                        .activationTime(now)
                        .issueTime(now)
                        .expirationTime(
                                ZoneDateTimeUtils.min(now.plusSeconds(client.getAccessTokenTTLSeconds()),
                                        authorization.getExpiryTime())
                        )
                        .labels(List.of("access_token"))
                        .provider(configuration.getAccessTokenProvider())
                        .type("access_token")
                        .category(client.getTokenCategory())
                        .active(true)
                        .build()
        );
        authorization.addAccessToken(accessToken);
        return accessToken;
    }

    private Optional<Token> createRefreshTokenIfEnabled(GrantAuthorization authorization, MmaduClient client,
                                                        DomainIdentityConfiguration configuration) {
        if (!configuration.isRefreshTokenEnabled() && client.issueRefreshTokens()) {
            return Optional.empty();
        }
        ZonedDateTime now = ZonedDateTime.now();
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
                                ZoneDateTimeUtils.min(now.plusSeconds(client.getRefreshTokenTTLSeconds()), authorization.getExpiryTime())
                        )
                        .type("refresh_token")
                        .category(client.getTokenCategory())
                        .active(true)
                        .build()
        );
        authorization.addRefreshToken(refreshToken);
        return Optional.of(refreshToken);
    }

    private static TokenException unauthorizedClient() {
        return new TokenException(new UnauthorizedClient("invalid.credentials", ""));
    }

}
