package com.mmadu.identity.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import com.mmadu.identity.providers.credentials.CredentialDataHashProvider;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientSecretCredentials.class, name = "secret")
})
public interface ClientCredentials extends Serializable {

    String getType();

    boolean matches(Object credential, CredentialDataHashMatcher matcher);

    void hashData(CredentialDataHashProvider provider);
}
