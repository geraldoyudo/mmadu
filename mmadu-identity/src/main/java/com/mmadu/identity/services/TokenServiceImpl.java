package com.mmadu.identity.services;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.providers.token.TokenCreationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private TokenCreationProvider tokenCreationProvider;
    private MmaduClient mmaduClient;

    @Autowired
    public void setTokenCreationProvider(TokenCreationProvider tokenCreationProvider) {
        this.tokenCreationProvider = tokenCreationProvider;
    }

    @Autowired
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Override
    public TokenResponse getToken(TokenRequest request) {
        return tokenCreationProvider.createToken(request, mmaduClient);
    }
}
