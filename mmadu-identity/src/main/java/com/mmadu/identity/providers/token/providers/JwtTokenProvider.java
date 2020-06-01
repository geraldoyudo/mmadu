package com.mmadu.identity.providers.token.providers;

import com.mmadu.identity.entities.token.TokenCredentials;
import com.mmadu.identity.models.token.TokenSpecification;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {
    public static final String TYPE = "jwt";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public TokenCredentials create(TokenSpecification specification) {

        return null;
    }
}
