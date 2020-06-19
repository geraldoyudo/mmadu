package com.mmadu.identity.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmadu.identity.providers.client.instance.CredentialDataHashMatcher;
import com.mmadu.identity.providers.client.instance.CredentialDataHashProvider;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secret;

    @Override
    public String getType() {
        return "secret";
    }

    @Override
    public boolean matches(Object credential, CredentialDataHashMatcher matcher) {
        if (credential instanceof String) {
            return matcher.matches((String) credential, secret);
        }
        return false;
    }

    @Override
    public void hashData(CredentialDataHashProvider provider) {
        secret = provider.hash(secret);
    }
}
