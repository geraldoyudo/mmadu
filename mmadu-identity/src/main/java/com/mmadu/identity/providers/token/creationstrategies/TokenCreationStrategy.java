package com.mmadu.identity.providers.token.creationstrategies;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;

public interface TokenCreationStrategy {

    boolean apply(TokenRequest request, MmaduClient client);

    TokenResponse getToken(TokenRequest request, MmaduClient client);
}
