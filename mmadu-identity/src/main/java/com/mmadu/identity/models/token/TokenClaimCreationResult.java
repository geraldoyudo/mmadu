package com.mmadu.identity.models.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenClaimCreationResult {
    private TokenClaim claim;
    private TokenSpecification specification;
}
