package com.mmadu.identity.services.authorization;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import com.mmadu.identity.models.authorization.AuthorizationResponse;

public interface AuthorizationService {

    String processAuthorization(AuthorizationRequest request, AuthorizationResponse response);
}
