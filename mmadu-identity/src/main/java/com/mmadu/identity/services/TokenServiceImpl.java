package com.mmadu.identity.services;

import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.InvalidClient;
import com.mmadu.identity.providers.token.TokenCreationProvider;
import com.mmadu.identity.services.client.MmaduClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private TokenCreationProvider tokenCreationProvider;
    private MmaduClient mmaduClient;
    private MmaduClientService mmaduClientService;

    @Autowired
    public void setTokenCreationProvider(TokenCreationProvider tokenCreationProvider) {
        this.tokenCreationProvider = tokenCreationProvider;
    }

    @Autowired
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Override
    public TokenResponse getToken(TokenRequest request) {
        MmaduClient client = mmaduClient;
        if (client.getClientIdentifier() == null) {
            client = mmaduClientService.loadClientByIdentifier(request.getClient_id())
                    .orElseThrow(() -> new TokenException(new InvalidClient("client not found", "")));
        }
        return tokenCreationProvider.createToken(request, client);
    }
}
