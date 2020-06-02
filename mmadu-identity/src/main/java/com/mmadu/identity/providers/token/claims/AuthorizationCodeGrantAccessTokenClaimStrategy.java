package com.mmadu.identity.providers.token.claims;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenClaim;
import com.mmadu.identity.utils.GrantTypeUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class AuthorizationCodeGrantAccessTokenClaimStrategy implements ClaimGenerationStrategy {
    @Override
    public boolean apply(GrantAuthorization authorization, ClaimSpecs specs) {
        return GrantTypeUtils.AUTHORIZATION_CODE.equals(authorization.getGrantType()) &&
                "access_token".equals(specs.getType());
    }

    @Override
    public TokenClaim generateClaim(GrantAuthorization authorization, ClaimSpecs specs) {
        return AuthorizationCodeAccessTokenClaim.builder()
                .build();
    }

    @Data
    @Builder
    public static class AuthorizationCodeAccessTokenClaim implements TokenClaim {
        private String issuer;
        private String subject;
        private String audience;
        private ZonedDateTime expirationTime;
        private ZonedDateTime activationTime;
        private ZonedDateTime issueTime;
        private String tokenIdentifier;
        private String clientIdentifier;
    }
}
