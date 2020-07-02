package com.mmadu.identity.providers.token.claims;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.exceptions.ClientInstanceNotFoundException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.ClaimConfiguration;
import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenClaim;
import com.mmadu.identity.models.token.TokenSpecification;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserGrantAccessTokenClaimStrategy implements ClaimGenerationStrategy {
    private static final List<String> USER_GRANTS = List.of(
            GrantTypeUtils.AUTHORIZATION_CODE, GrantTypeUtils.IMPLICIT, GrantTypeUtils.PASSWORD
    );

    private MmaduClientService mmaduClientService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public boolean apply(TokenSpecification tokenSpecs, ClaimSpecs specs) {
        return USER_GRANTS.contains(tokenSpecs.getGrantAuthorization().getGrantType()) &&
                "access_token".equals(specs.getType());
    }

    @Override
    public TokenClaim generateClaim(TokenSpecification tokenSpecs, ClaimSpecs specs) {
        GrantAuthorization authorization = tokenSpecs.getGrantAuthorization();
        MmaduClient client = mmaduClientService.loadClientByIdentifier(authorization.getClientIdentifier())
                .orElseThrow(ClientInstanceNotFoundException::new);
        ClaimConfiguration configuration = specs.getConfiguration();
        List<String> scopes;
        if (tokenSpecs.getScopes() == null || tokenSpecs.getScopes().isEmpty()) {
            scopes = authorization.getScopes();
        } else {
            scopes = tokenSpecs.getScopes();
        }
        return UserAccessTokenClaim.builder()
                .issuer(configuration.getIssuer())
                .subject(authorization.getId())
                .activationTime(tokenSpecs.getActivationTime())
                .expirationTime(tokenSpecs.getExpirationTime())
                .issueTime(tokenSpecs.getIssueTime())
                .clientIdentifier(authorization.getClientIdentifier())
                .audience(client.getResources())
                .tokenIdentifier(specs.getId())
                .domainId(authorization.getDomainId())
                .userId(authorization.getUserId())
                .scope(
                        Optional.ofNullable(scopes).orElse(Collections.emptyList())
                                .stream()
                                .collect(Collectors.joining(" "))
                )
                .authorities(client.isIncludeUserAuthorities() ? authorization.getUserAuthorities() : null)
                .roles(client.isIncludeUserRoles() ? authorization.getUserRoles() : null)
                .groups(client.isIncludeUserGroups() ? authorization.getUserGroups() : null)
                .build();
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserAccessTokenClaim implements TokenClaim {
        private String issuer;
        private String subject;
        private List<String> audience;
        private ZonedDateTime expirationTime;
        private ZonedDateTime activationTime;
        private ZonedDateTime issueTime;
        private String tokenIdentifier;
        private String clientIdentifier;
        private String domainId;
        private String scope;
        @JsonProperty("user_id")
        private String userId;
        private List<String> authorities;
        private List<String> roles;
        private List<String> groups;
    }
}
