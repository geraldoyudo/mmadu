package com.mmadu.identity.security.clients.strategies;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import com.mmadu.identity.security.clients.ClientCredentialRequestAuthenticationStrategy;
import com.mmadu.identity.security.clients.ClientCredentialRequestPayload;
import com.mmadu.identity.security.clients.ClientSecretCredentialRequestPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
@Order(10)
public class ClientSecretRequestAuthenticationStrategy implements ClientCredentialRequestAuthenticationStrategy {
    private CredentialDataHashMatcher credentialDataHashMatcher;

    @Autowired
    public void setCredentialDataHashMatcher(CredentialDataHashMatcher credentialDataHashMatcher) {
        this.credentialDataHashMatcher = credentialDataHashMatcher;
    }

    @Override
    public boolean apply(HttpServletRequest request, ClientCredentialRequestPayload payload, MmaduClient client) {
        return (payload instanceof ClientSecretCredentialRequestPayload) &&
                !StringUtils.isEmpty(((ClientSecretCredentialRequestPayload) payload).getClientSecret()) &&
                client.getCredentials() != null &&
                (client.getCredentials() instanceof ClientSecretCredentials);
    }

    @Override
    public boolean isPermitted(HttpServletRequest request, ClientCredentialRequestPayload payload, MmaduClient client) {
        ClientSecretCredentialRequestPayload secretPayload = (ClientSecretCredentialRequestPayload) payload;
        return client.getCredentials().matches(secretPayload.getClientSecret(), credentialDataHashMatcher);
    }
}
