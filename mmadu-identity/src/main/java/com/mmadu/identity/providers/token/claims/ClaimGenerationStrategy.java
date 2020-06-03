package com.mmadu.identity.providers.token.claims;

import com.mmadu.identity.models.token.ClaimSpecs;
import com.mmadu.identity.models.token.TokenClaim;
import com.mmadu.identity.models.token.TokenSpecification;

public interface ClaimGenerationStrategy {
    boolean apply(TokenSpecification tokenSpecs, ClaimSpecs specs);

    TokenClaim generateClaim(TokenSpecification tokenSpecs, ClaimSpecs specs);
}
