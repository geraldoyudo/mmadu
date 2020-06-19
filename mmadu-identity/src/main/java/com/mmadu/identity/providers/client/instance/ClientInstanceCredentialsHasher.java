package com.mmadu.identity.providers.client.instance;

import com.mmadu.identity.entities.ClientCredentials;
import com.mmadu.identity.entities.ClientInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ClientInstanceCredentialsHasher {
    private CredentialDataHashProvider hashProvider;

    @Autowired
    public void setHashProvider(CredentialDataHashProvider hashProvider) {
        this.hashProvider = hashProvider;
    }

    @HandleBeforeSave
    public void hashCredentialsBeforeSave(ClientInstance instance) {
        hashCredentials(instance.getCredentials());
    }

    @HandleBeforeCreate
    public void hashCredentialsBeforeCreate(ClientInstance instance) {
        hashCredentials(instance.getCredentials());
    }

    private void hashCredentials(ClientCredentials credentials) {
        credentials.hashData(hashProvider);
    }
}
