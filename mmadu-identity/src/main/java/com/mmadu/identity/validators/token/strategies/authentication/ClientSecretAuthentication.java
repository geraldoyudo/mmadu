package com.mmadu.identity.validators.token.strategies.authentication;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order(10)
public class ClientSecretAuthentication implements ClientTokenRequestAuthenticationStrategy {
    private CredentialDataHashMatcher credentialDataHashMatcher;

    @Autowired
    public void setCredentialDataHashMatcher(CredentialDataHashMatcher credentialDataHashMatcher) {
        this.credentialDataHashMatcher = credentialDataHashMatcher;
    }

    @Override
    public boolean apply(TokenRequest request, MmaduClient client) {
        return !StringUtils.isEmpty(request.getClient_secret()) &&
                client.getCredentials() != null && (client.getCredentials() instanceof ClientSecretCredentials);
    }

    @Override
    public boolean isPermitted(TokenRequest request, MmaduClient client) {
        return client.getCredentials().matches(request.getClient_secret(), credentialDataHashMatcher);
    }
}
