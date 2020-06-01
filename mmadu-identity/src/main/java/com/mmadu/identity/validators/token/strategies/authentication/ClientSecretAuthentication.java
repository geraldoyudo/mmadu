package com.mmadu.identity.validators.token.strategies.authentication;

import com.mmadu.identity.entities.ClientSecretCredentials;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Order(10)
public class ClientSecretAuthentication implements ClientTokenRequestAuthenticationStrategy {

    @Override
    public boolean apply(TokenRequest request, MmaduClient client) {
        return !StringUtils.isEmpty(request.getClientSecret()) &&
                client.getCredentials() != null && (client.getCredentials() instanceof ClientSecretCredentials);
    }

    @Override
    public boolean isPermitted(TokenRequest request, MmaduClient client) {
        return client.getCredentials().matches(request.getClientSecret());
    }
}
