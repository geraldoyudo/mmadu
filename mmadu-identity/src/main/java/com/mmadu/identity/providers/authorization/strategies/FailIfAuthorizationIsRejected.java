package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.authorization.errors.AccessDenied;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class FailIfAuthorizationIsRejected implements AuthorizationStrategy {
    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return true;
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        if(!response.isAuthorize()){
            AccessDenied accessDenied = new AccessDenied();
            accessDenied.setErrorDescription("access was denied by user");
            context.fail(accessDenied);
        }
    }
}
