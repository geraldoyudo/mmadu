package com.mmadu.identity.providers.credentials;

import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.entities.credentials.Credential;
import com.mmadu.identity.entities.credentials.CredentialData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CredentialsHasher {
    private CredentialDataHashProvider hashProvider;
    private CredentialEncryptionProvider encryptionProvider;

    @Autowired
    public void setHashProvider(CredentialDataHashProvider hashProvider) {
        this.hashProvider = hashProvider;
    }

    @Autowired
    public void setEncryptionProvider(CredentialEncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
    }

    @HandleBeforeSave
    public void hashCredentialsBeforeSave(Credential credential) {
        hashCredentials(credential.getData());
    }

    @HandleBeforeCreate
    public void hashCredentialsBeforeCreate(Credential credential) {
        hashCredentials(credential.getData());
    }

    private void hashCredentials(CredentialData credentials) {
        credentials.hashData(hashProvider);
        credentials.encryptData(encryptionProvider);
    }
}
