package com.mmadu.identity.security.clients;

import com.mmadu.identity.exceptions.AuthenticationException;
import com.mmadu.identity.exceptions.AuthorizationException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.services.client.MmaduClientService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Aspect
@Component
public class ClientAuthenticationEnforcer {
    private MmaduClient mmaduClient;
    private MmaduClientService mmaduClientService;
    private ClientRequestAuthenticator clientRequestAuthenticator;

    @Autowired
    public void setMmaduClient(MmaduClient mmaduClient) {
        this.mmaduClient = mmaduClient;
    }

    @Autowired
    public void setClientRequestAuthenticator(ClientRequestAuthenticator clientRequestAuthenticator) {
        this.clientRequestAuthenticator = clientRequestAuthenticator;
    }

    @Autowired
    public void setMmaduClientService(MmaduClientService mmaduClientService) {
        this.mmaduClientService = mmaduClientService;
    }

    @Before("@annotation(authentication) && args(payload,..)")
    public void ensureClientAuthentication(EnsureClientAuthentication authentication, ClientCredentialRequestPayload payload) {
        if (!StringUtils.isEmpty(mmaduClient.getClientIdentifier())) {
            return;
        }
        if (StringUtils.isEmpty(payload.getClientIdentifier())) {
            throw new AuthorizationException("client.id.required");
        } else {
            Optional<MmaduClient> clientOptional = mmaduClientService.loadClientByIdentifier(payload.getClientIdentifier());
            if (clientOptional.isEmpty()) {
                throw new AuthorizationException("client.not.found");
            } else {
                MmaduClient client = clientOptional.get();
                try {
                    clientRequestAuthenticator.authenticateClient(payload, client);
                } catch (AuthenticationException ex) {
                    throw new AuthorizationException("unauthorized");
                }
            }
        }
    }
}
