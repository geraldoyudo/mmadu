package com.mmadu.identity.providers.token.claims;

import com.mmadu.identity.entities.GrantAuthorization;
import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenClaim;

public interface ClaimGenerationStrategy {
    boolean apply(GrantAuthorization authorization, ClaimSpecs specs);

    TokenClaim generateClaim(GrantAuthorization authorization, ClaimSpecs specs);
}
