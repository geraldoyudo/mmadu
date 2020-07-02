package com.mmadu.identity.providers.token;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;

public interface TokenCreationProvider {

    TokenResponse createToken(TokenRequest request, TokenContext context);
}
