package com.mmadu.identity.entities.token;

import lombok.Data;

@Data
public class JwtTokenCredentials implements TokenCredentials, HasBasicTokenData {
    private String token;

    @Override
    public String getType() {
        return "jwt";
    }
}
