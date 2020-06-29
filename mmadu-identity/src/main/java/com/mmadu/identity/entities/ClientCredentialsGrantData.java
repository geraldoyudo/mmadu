package com.mmadu.identity.entities;

public class ClientCredentialsGrantData implements GrantData {
    @Override
    public String getType() {
        return "client_credentials";
    }
}
