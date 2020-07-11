package com.mmadu.identity.services.authorization;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.scopes.ProposedScopeFilter;
import com.mmadu.identity.providers.authorization.scopes.ScopeFilterContext;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProposedScopeLimitServiceImpl implements ProposedScopeLimitService {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private List<ProposedScopeFilter> proposedScopeFilters = Collections.emptyList();

    @Autowired(required = false)
    public void setProposedScopeFilters(List<ProposedScopeFilter> proposedScopeFilters) {
        this.proposedScopeFilters = proposedScopeFilters;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> limitScopesForUser(List<String> scopes, MmaduUser user, MmaduClient client) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(user.getDomainId())
                .orElseThrow(() -> new DomainNotFoundException("user domain not found"));
        Set<String> approvedScopes = new HashSet<>(scopes);
        final ScopeFilterContext context = ScopeFilterContext.builder()
                .client(client)
                .configuration(configuration)
                .user(user)
                .build();
        List<ProposedScopeFilter> filtersToApply = proposedScopeFilters.stream()
                .filter(f -> f.apply(configuration, user, client))
                .collect(Collectors.toList());
        for (ProposedScopeFilter filter : filtersToApply) {
            approvedScopes = Set.copyOf(filter.filter(List.copyOf(approvedScopes), context));
        }
        return List.copyOf(approvedScopes);
    }
}
