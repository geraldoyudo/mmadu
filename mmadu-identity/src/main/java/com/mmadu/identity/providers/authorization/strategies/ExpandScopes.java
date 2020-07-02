package com.mmadu.identity.providers.authorization.strategies;

import com.mmadu.identity.models.authorization.AuthorizationContext;
import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.services.authorization.ApprovedScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(50)
public class ExpandScopes implements AuthorizationStrategy {
    private static final List<String> RESPONSE_TYPES = List.of("code");
    private ApprovedScopeService approvedScopeService;

    @Autowired
    public void setApprovedScopeService(ApprovedScopeService approvedScopeService) {
        this.approvedScopeService = approvedScopeService;
    }

    @Override
    public boolean apply(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        return RESPONSE_TYPES.contains(request.getResponse_type());
    }

    @Override
    public void authorize(AuthorizationRequest request, AuthorizationResponse response, AuthorizationContext context) {
        if (context.getAuthorizer() instanceof MmaduUser) {
            List<String> derivedScopes = approvedScopeService.processScopesForUser(response.getScopes(),
                    (MmaduUser) context.getAuthorizer(),
                    context.getClient());
            Set<String> resultantScopes = new HashSet<>();
            resultantScopes.addAll(response.getScopes());
            resultantScopes.addAll(derivedScopes);
            response.setScopes(List.copyOf(resultantScopes));
        }
    }

}
