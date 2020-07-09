package com.mmadu.identity.services.token;

import com.mmadu.identity.models.token.TokenRevocationRequest;

public interface TokenRevocationService {

    void revokeToken(TokenRevocationRequest request);
}
