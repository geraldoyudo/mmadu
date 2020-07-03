package com.mmadu.identity.providers.authorization.scopes;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.repositories.ScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Order(200)
public class RoleAndAuthoritiesApprovedScopeFilter implements ApprovedScopeFilter {
    private ScopeRepository scopeRepository;

    @Autowired
    public void setScopeRepository(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public boolean apply(DomainIdentityConfiguration configuration, MmaduUser user, MmaduClient client) {
        return true;
    }

    @Override
    public List<String> filter(List<String> scopes, ScopeFilterContext context) {
        List<String> proposedScopes = new LinkedList<>(scopes);
        scopeRepository.findByDomainIdAndCodeIn(context.getConfiguration().getDomainId(),
                scopes)
                .stream()
                .flatMap(scope -> Optional.ofNullable(scope.getAuthorities()).orElse(Collections.emptyList()).stream())
                .forEach(proposedScopes::add);
        MmaduUser user = context.getUser();
        List<String> rolesAndAuthorities = new LinkedList<>(scopes);
        rolesAndAuthorities.addAll(user.getAuthorities());
        rolesAndAuthorities.addAll(user.getRoles());
        return proposedScopes.stream()
                .filter(rolesAndAuthorities::contains)
                .collect(Collectors.toList());
    }
}
