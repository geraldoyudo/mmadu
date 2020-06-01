package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;

public interface ClientTokenRequestAuthenticator {
    void authenticatePublicClient(TokenRequest request, MmaduClient client);
}
