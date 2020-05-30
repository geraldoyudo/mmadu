package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.models.authorization.AuthorizationCodeRedirectData;
import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationCodeStrategy implements AuthorizationStrategy {
    private static final String RESPONSE_TYPE = "code";

    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response) {
        return RESPONSE_TYPE.equals(request.getResponse_type());
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        AuthorizationCodeRedirectData data = new AuthorizationCodeRedirectData();
        data.setCode("ADe2ea");
        data.setState(request.getState());
        context.succeed(data);
    }
}
