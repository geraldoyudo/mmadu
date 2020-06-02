package com.mmadu.identity.entities.token;

import lombok.Data;

@Data
public class JwtTokenCredentials implements TokenCredentials, HasBasicTokenData, HasTokenIdentifier {
    private String token;
    private String jti;

    @Override
    public String getType() {
        return "jwt";
    }

    @Override
    public String toString() {
        return token;
    }

    @Override
    public String getTokenIdentifier() {
        return jti;
    }
}
