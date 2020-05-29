package com.mmadu.identity.entities;

import lombok.Data;

@Data
public class ClientSecretCredentials implements ClientCredentials {
    private String secret;
}
