package com.mmadu.identity.entities.token;

import lombok.Data;

@Data
public class AlphanumericTokenCredentials implements TokenCredentials {
    private String token;

    @Override
    public String getType() {
        return token;
    }

    @Override
    public String toString() {
        return token;
    }

    @Override
    public String getTokenString() {
        return token;
    }

    @Override
    public String getTokenIdentifier() {
        return token;
    }
}
