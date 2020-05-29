package com.mmadu.identity.utils;

import com.mmadu.identity.entities.ClientCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCredentialsRegistration {
    private String name;
    private Class<? extends ClientCredentials> type;
}
