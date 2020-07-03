package com.mmadu.identity.models.token;

import com.mmadu.identity.entities.token.TokenCredentials;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenCreationResult {
    private TokenCredentials credentials;
    private TokenSpecification specification;
}
