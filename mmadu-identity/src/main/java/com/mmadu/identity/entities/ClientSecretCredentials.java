package com.mmadu.identity.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClientSecretCredentials implements ClientCredentials {
    private String secret;

    @Override
    public String getType() {
        return "secret";
    }
}
