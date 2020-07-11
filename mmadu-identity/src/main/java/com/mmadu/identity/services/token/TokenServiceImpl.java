package com.mmadu.identity.services.token;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.entities.Token;
import com.mmadu.identity.exceptions.TokenException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenContext;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.models.token.TokenResponse;
import com.mmadu.identity.models.token.error.InvalidClient;
import com.mmadu.identity.providers.token.TokenCreationProvider;
import com.mmadu.identity.services.client.MmaduClientService;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import com.mmadu.identity.services.token.TokenService;
import com.mmadu.identity.utils.TokenErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {
    private TokenCreationProvider tokenCreationProvider;
    private MmaduClient mmaduClient;
    private MmaduClientService mmaduClientService;
    private DomainIdentityConfigurationService domainIdentityConfigurationService;

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

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    @Transactional
    public TokenResponse getToken(TokenRequest request) {
        MmaduClient client = mmaduClient;
        if (client.getClientIdentifier() == null) {
            client = mmaduClientService.loadClientByIdentifier(request.getClient_id())
                    .orElseThrow(TokenErrorUtils::invalidClient);
        }
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(client.getDomainId())
                .orElseThrow(TokenErrorUtils::invalidClient);
        TokenContext context = TokenContext.builder()
                .client(client)
                .configuration(configuration)
                .build();
        return tokenCreationProvider.createToken(request, context);
    }
}
