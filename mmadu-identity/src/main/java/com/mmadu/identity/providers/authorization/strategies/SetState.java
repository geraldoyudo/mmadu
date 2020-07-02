package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class SetState implements AuthorizationStrategy {
    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return true;
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        context.setState(request.getState());
    }
}
