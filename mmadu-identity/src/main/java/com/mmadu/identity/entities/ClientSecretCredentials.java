package com.mmadu.identity.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ClientSecretCredentials implements ClientCredentials {
    private String secret;
}
