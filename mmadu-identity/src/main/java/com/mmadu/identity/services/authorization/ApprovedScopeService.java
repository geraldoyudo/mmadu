package com.mmadu.identity.services.authorization;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;

import java.util.List;

public interface ApprovedScopeService {

    List<String> processScopesForUser(List<String> scopes, MmaduUser user, MmaduClient client);
}
