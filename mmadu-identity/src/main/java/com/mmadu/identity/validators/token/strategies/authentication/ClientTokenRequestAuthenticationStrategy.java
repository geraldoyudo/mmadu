package com.mmadu.identity.validators.token.strategies.authentication;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.token.TokenRequest;

public interface ClientTokenRequestAuthenticationStrategy {

    boolean apply(TokenRequest request, MmaduClient client);

    boolean isPermitted(TokenRequest request, MmaduClient client);
}
