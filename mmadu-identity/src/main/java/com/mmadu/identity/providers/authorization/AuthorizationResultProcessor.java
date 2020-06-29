package com.mmadu.identity.providers.authorization;

import com.mmadu.identity.models.authorization.AuthorizationResult;

public interface AuthorizationResultProcessor {

    String processResult(AuthorizationResult result);
}
