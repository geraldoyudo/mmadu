package com.mmadu.identity.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClientSecretCredentials implements ClientCredentials {
    @NotEmpty(message = "secret cannot be empty")
    private String secret;

    @Override
    public String getType() {
        return "secret";
    }
}
