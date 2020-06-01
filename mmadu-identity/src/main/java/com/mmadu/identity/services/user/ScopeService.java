package com.mmadu.identity.services.user;

import com.mmadu.identity.entities.Scope;

import java.util.List;

public interface ScopeService {

    boolean areAllSupportedByDomain(String domainId, List<String> scopes);

    List<Scope> getAllScopeInfo(String domainId, List<String> scopes);
}
