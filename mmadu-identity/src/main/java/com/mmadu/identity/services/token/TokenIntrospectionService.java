package com.mmadu.identity.services.token;

import com.mmadu.identity.models.token.TokenIntrospectionRequest;
import com.mmadu.identity.models.token.TokenIntrospectionResponse;

public interface TokenIntrospectionService {

    TokenIntrospectionResponse getTokenDetails(TokenIntrospectionRequest request);

}
