package com.mmadu.identity.services.authorization;

import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;

import java.util.List;

public interface ScopeLimitService {

    List<String> limitScopesForUser(List<String> scopes, MmaduUser user, MmaduClient client);
}
