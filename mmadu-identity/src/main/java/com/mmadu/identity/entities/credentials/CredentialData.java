package com.mmadu.identity.entities.credentials;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mmadu.identity.providers.credentials.CredentialDataHashMatcher;
import com.mmadu.identity.providers.credentials.CredentialDataHashProvider;
import com.mmadu.identity.providers.credentials.CredentialDecryptionProvider;
import com.mmadu.identity.providers.credentials.CredentialEncryptionProvider;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RSACredentialData.class, name = "rsa")
})
public interface CredentialData {
    String getType();

    boolean matches(Object credential, CredentialDataHashMatcher matcher);

    void hashData(CredentialDataHashProvider provider);

    void encryptData(CredentialEncryptionProvider provider);

    void decryptData(CredentialDecryptionProvider provider);
}
