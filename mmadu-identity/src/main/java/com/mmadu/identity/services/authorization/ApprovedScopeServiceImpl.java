package com.mmadu.identity.services.authorization;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import com.mmadu.identity.providers.authorization.scopes.ApprovedScopeFilter;
import com.mmadu.identity.providers.authorization.scopes.ScopeFilterContext;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApprovedScopeServiceImpl implements ApprovedScopeService {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private List<ApprovedScopeFilter> approvedScopeFilters = Collections.emptyList();

    @Autowired(required = false)
    public void setApprovedScopeFilters(List<ApprovedScopeFilter> approvedScopeFilters) {
        this.approvedScopeFilters = approvedScopeFilters;
    }

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Override
    public List<String> processScopesForUser(List<String> scopes, MmaduUser user, MmaduClient client) {
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(user.getDomainId())
                .orElseThrow(() -> new DomainNotFoundException("user domain not found"));
        Set<String> approvedScopes = new HashSet<>(scopes);
        final ScopeFilterContext context = ScopeFilterContext.builder()
                .client(client)
                .configuration(configuration)
                .user(user)
                .build();
        List<ApprovedScopeFilter> filtersToApply = approvedScopeFilters.stream()
                .filter(f -> f.apply(configuration, user, client))
                .collect(Collectors.toList());
        for (ApprovedScopeFilter filter : filtersToApply) {
            approvedScopes = Set.copyOf(filter.filter(List.copyOf(approvedScopes), context));
        }
        return List.copyOf(approvedScopes);
    }
}
