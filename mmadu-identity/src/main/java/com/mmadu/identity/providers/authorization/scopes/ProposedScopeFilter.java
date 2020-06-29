package com.mmadu.identity.providers.authorization.scopes;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;

import java.util.List;

public interface ProposedScopeFilter {

    boolean apply(DomainIdentityConfiguration configuration,
                  MmaduUser user,
                  MmaduClient client);

    List<String> filter(List<String> scopes, ScopeFilterContext context);
}
