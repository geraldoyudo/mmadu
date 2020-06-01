package com.mmadu.identity.services;

import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;

public interface TokenService {

    TokenResponse getToken(TokenRequest request);
}
