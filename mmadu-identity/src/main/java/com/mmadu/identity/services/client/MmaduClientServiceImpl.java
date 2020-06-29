package com.mmadu.identity.services.client;

import com.mmadu.identity.entities.Client;
import com.mmadu.identity.entities.ClientInstance;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.client.MmaduClientImpl;
import com.mmadu.identity.repositories.ClientInstanceRepository;
import com.mmadu.identity.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MmaduClientServiceImpl implements MmaduClientService {
    private ClientInstanceRepository clientInstanceRepository;
    private ClientRepository clientRepository;

    @Autowired
    public void setClientInstanceRepository(ClientInstanceRepository clientInstanceRepository) {
        this.clientInstanceRepository = clientInstanceRepository;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Cacheable("clients")
    public Optional<MmaduClient> loadClientByIdentifier(String clientIdentifier) {
        Optional<ClientInstance> optionalInstance = clientInstanceRepository.findByIdentifier(clientIdentifier);
        if (optionalInstance.isEmpty()) {
            return Optional.empty();
        }
        ClientInstance clientInstance = optionalInstance.get();
        Optional<Client> optionalClient = clientRepository.findById(clientInstance.getClientId());
        if (optionalClient.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new MmaduClientImpl(optionalClient.get(), clientInstance));
    }
}
