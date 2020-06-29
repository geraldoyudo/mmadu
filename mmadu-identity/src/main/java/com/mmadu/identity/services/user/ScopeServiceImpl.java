package com.mmadu.identity.services.user;

import com.mmadu.identity.entities.Scope;
import com.mmadu.identity.repositories.ScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScopeServiceImpl implements ScopeService {
    private ScopeRepository scopeRepository;

    @Autowired
    public void setScopeRepository(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public boolean areAllSupportedByDomain(String domainId, List<String> scopes) {
        Set<String> uniqueScopes = new HashSet<>(Optional.ofNullable(scopes).orElse(Collections.emptyList()));
        return scopeRepository.countByDomainIdAndCodeIn(domainId, scopes) == uniqueScopes.size();
    }

    @Override
    public List<Scope> getAllScopeInfo(String domainId, List<String> scopes) {
        return scopeRepository.findByDomainIdAndCodeIn(domainId, scopes);
    }
}
