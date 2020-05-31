package com.mmadu.identity.providers.users;

import com.mmadu.identity.entities.Scope;

import java.util.List;

public interface ScopeService {

    boolean areAllSupportedByDomain(String domainId, List<String> scopes);

    List<Scope> getAllScopeInfo(String domainId, List<String> scopes);
}
