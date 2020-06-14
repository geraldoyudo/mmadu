package com.mmadu.identity.providers.authorization.scopes;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Order(100)
public class RoleAndAuthoritiesPopulatorFilter implements ScopeFilter {
    @Override
    public boolean apply(DomainIdentityConfiguration configuration, MmaduUser user, MmaduClient client) {
        return true;
    }

    @Override
    public List<String> filter(List<String> scopes, ScopeFilterContext context) {

        MmaduUser user = context.getUser();
        if (scopes.isEmpty()) {
            List<String> rolesAndAuthorities = new LinkedList<>();
            rolesAndAuthorities.addAll(user.getAuthorities());
            rolesAndAuthorities.addAll(user.getRoles());
            return rolesAndAuthorities;
        } else {
            return scopes;
        }
    }
}
