package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.authorization.ImplicitRedirectData;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.token.TokenFactory;
import com.mmadu.identity.repositories.GrantAuthorizationRepository;
import com.mmadu.identity.utils.AuthorizationUtils;
import com.mmadu.identity.utils.GrantTypeUtils;
import com.mmadu.identity.utils.ZoneDateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.mmadu.identity.entities.NoGrantData.noGrantData;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.min;

@Component
public class ImplicitStrategy implements AuthorizationStrategy {
    private static final String TOKEN_RESPONSE_TYPE = "token";

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
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return TOKEN_RESPONSE_TYPE.equals(request.getResponse_type()) &&
                Optional.ofNullable(context.getClient().getSupportedGrantTypes())
                        .orElse(emptyList())
                        .contains(GrantTypeUtils.IMPLICIT);
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        AuthorizationUtils.ensureMmaduUserAuthorizer(context);
        MmaduUser authorizer = (MmaduUser) context.getAuthorizer();
        DomainIdentityConfiguration configuration = context.getDomainConfiguration();
        MmaduClient client = context.getClient();
        GrantAuthorization grantAuthorization = createBaseAuthorization(request, response, context, authorizer, client);
        ZonedDateTime now = ZonedDateTime.now();
        grantAuthorization.setIssuedTime(now);
        grantAuthorization.setExpiryTime(
                now.plusSeconds(min(configuration.getMaxAuthorizationTTLSeconds(), client.getImplicitGrantTypeTTLSeconds()))
        );
        grantAuthorization.setData(noGrantData());
        grantAuthorization = grantAuthorizationRepository.save(grantAuthorization);
        Token token = generateAccessTokenFromAuthorization(grantAuthorization, configuration, client);
        grantAuthorization.addAccessToken(token);
        grantAuthorization.setActive(true);
        grantAuthorizationRepository.save(grantAuthorization);
        context.succeed(ImplicitRedirectData.fromTokenAndAuthorization(token, grantAuthorization));
    }

    private GrantAuthorization createBaseAuthorization(AuthorizationRequest request, AuthorizationResponse response,
                                                       AuthorizationContext context, MmaduUser authorizer, MmaduClient client) {
        GrantAuthorization grantAuthorization = new GrantAuthorization();
        grantAuthorization.setClientInstanceId(client.getInstanceId());
        grantAuthorization.setClientId(client.getId());
        grantAuthorization.setDomainId(authorizer.getDomainId());
        grantAuthorization.setUserId(authorizer.getId());
        grantAuthorization.setUsername(authorizer.getUsername());
        grantAuthorization.setUserRoles(authorizer.getRoles());
        grantAuthorization.setUserGroups(authorizer.getGroups());
        grantAuthorization.setUserAuthorities(authorizer.getAuthorities());
        grantAuthorization.setScopes(response.getScopes());
        grantAuthorization.setRedirectUri(context.getResult().getRedirectUri());
        grantAuthorization.setRedirectUriSpecified(context.getResult().isRedirectUriSpecified());
        grantAuthorization.setClientIdentifier(client.getClientIdentifier());
        grantAuthorization.setState(request.getState());
        grantAuthorization.setGrantType(GrantTypeUtils.IMPLICIT);
        return grantAuthorization;
    }

    private Token generateAccessTokenFromAuthorization(GrantAuthorization authorization,
                                                       DomainIdentityConfiguration configuration, MmaduClient client) {
        ZonedDateTime now = ZonedDateTime.now();
        return tokenFactory.createToken(
                TokenSpecification.builder()
                        .configuration(configuration.getAccessTokenProperties())
                        .domainId(authorization.getDomainId())
                        .grantAuthorization(authorization)
                        .activationTime(now)
                        .issueTime(now)
                        .expirationTime(
                                ZoneDateTimeUtils.min(now.plusSeconds(client.getAccessTokenTTLSeconds()), authorization.getExpiryTime())
                        )
                        .labels(List.of("access_token"))
                        .provider(configuration.getAccessTokenProvider())
                        .type("access_token")
                        .category(client.getTokenCategory())
                        .active(true)
                        .build()
        );
    }

    @Override
    public boolean isGrantType() {
        return true;
    }
}
