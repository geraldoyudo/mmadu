package com.mmadu.identity.providers.token.claims;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.exceptions.ClientInstanceNotFoundException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.*;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class ClientCredentialsGrantAccessTokenClaimStrategy implements ClaimGenerationStrategy {
    private MmaduClientService mmaduClientService;

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public boolean apply(TokenSpecification tokenSpecs, ClaimSpecs specs) {
        return GrantTypeUtils.CLIENT_CREDENTIALS.equals(tokenSpecs.getGrantAuthorization().getGrantType()) &&
                "access_token".equals(specs.getType());
    }

    @Override
    public TokenClaimCreationResult generateClaim(TokenSpecification tokenSpecs, ClaimSpecs specs) {
        GrantAuthorization authorization = tokenSpecs.getGrantAuthorization();
        MmaduClient client = mmaduClientService.loadClientByIdentifier(authorization.getClientIdentifier())
                .orElseThrow(ClientInstanceNotFoundException::new);
        ClaimConfiguration configuration = specs.getConfiguration();

        TokenClaim claim =  ClientCredentialsAccessTokenClaim.builder()
                .issuer(configuration.getIssuer())
                .subject(authorization.getId())
                .activationTime(tokenSpecs.getActivationTime())
                .expirationTime(tokenSpecs.getExpirationTime())
                .issueTime(tokenSpecs.getIssueTime())
                .clientIdentifier(authorization.getClientIdentifier())
                .audience(client.getResources())
                .tokenIdentifier(specs.getId())
                .domainId(authorization.getDomainId())
                .authorities(client.getAuthorities())
                .build();
        return TokenClaimCreationResult.builder()
                .specification(tokenSpecs)
                .claim(claim)
                .build();
    }

    @Data
    @Builder
    public static class ClientCredentialsAccessTokenClaim implements TokenClaim {
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
        private List<String> authorities;
    }
}
