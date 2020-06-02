package com.mmadu.identity.entities.credentials;

import lombok.Data;

@Data
public class RSACredentialData implements CredentialData {
    private String keyData;

    @Override
    public String getType() {
        return "rsa";
    }
}
