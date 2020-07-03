package com.mmadu.identity.providers.token.providers;

import com.mmadu.identity.models.token.TokenCreationResult;
import com.mmadu.identity.models.token.TokenSpecification;

public interface TokenProvider {
    String providerId();

    TokenCreationResult create(TokenSpecification specification);
}
