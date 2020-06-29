package com.mmadu.identity.providers.token;

import com.mmadu.identity.entities.Token;
import com.mmadu.identity.models.token.TokenSpecification;

public interface TokenFactory {

    Token createToken(TokenSpecification spec);
}
