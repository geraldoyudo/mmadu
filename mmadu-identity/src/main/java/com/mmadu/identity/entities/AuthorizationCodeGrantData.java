package com.mmadu.identity.entities;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class AuthorizationCodeGrantData implements GrantData {
    private String code;
    private ZonedDateTime codeExpiryTime;

    @Override
    public String getType() {
        return "code";
    }
}
