package com.mmadu.identity.providers.token.providers;

import com.mmadu.identity.entities.token.TokenCredentials;
import com.mmadu.identity.models.token.TokenSpecification;

public interface TokenProvider {
    String type();

    TokenCredentials create(TokenSpecification specification);
}
