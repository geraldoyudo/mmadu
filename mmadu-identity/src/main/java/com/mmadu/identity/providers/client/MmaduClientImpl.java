package com.mmadu.identity.providers.client;

import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientInstance;

import java.util.Collections;
import java.util.List;

public class MmaduClientImpl implements MmaduClient {
    private Client client;
    private ClientInstance clientInstance;

    public MmaduClientImpl(Client client, ClientInstance clientInstance) {
        this.client = client;
        this.clientInstance = clientInstance;
    }

    @Override
    public String getDomainId() {
        return client.getDomainId();
    }

    @Override
    public String getClientIdentifier() {
        return clientInstance.getIdentifier();
    }

    @Override
    public List<String> getAuthorities() {
        return Collections.emptyList();
    }
}
