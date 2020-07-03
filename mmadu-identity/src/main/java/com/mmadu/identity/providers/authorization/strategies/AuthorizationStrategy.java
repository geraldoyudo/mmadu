package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;

public interface AuthorizationStrategy {
    boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context);

    void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context);

    default boolean isGrantType() {
        return false;
    }
}
