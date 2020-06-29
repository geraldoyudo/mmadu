package com.mmadu.identity.providers.authorization.scopes;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(100)
public class ClientProposedScopeLimitingFilter implements ProposedScopeFilter {
    @Override
    public boolean apply(DomainIdentityConfiguration configuration, MmaduUser user, MmaduClient client) {
        return true;
    }

    @Override
    public List<String> filter(List<String> scopes, ScopeFilterContext context) {
        List<String> availableScopes = Optional.ofNullable(context.getClient().getScopes()).orElse(Collections.emptyList());
        if (scopes.isEmpty()) {
            return availableScopes;
        } else {
            return scopes.stream()
                    .filter(availableScopes::contains)
                    .collect(Collectors.toList());
        }
    }
}
