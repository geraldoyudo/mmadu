package com.mmadu.identity.entities;

import lombok.Data;

@Data
public class JwtTokenCredentials implements TokenCredentials {
    private String token;

    @Override
    public String getType() {
        return "jwt";
    }
}
