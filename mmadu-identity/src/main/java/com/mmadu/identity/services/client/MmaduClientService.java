package com.mmadu.identity.services.client;

import com.mmadu.identity.models.client.MmaduClient;

import java.util.Optional;

public interface MmaduClientService {

    Optional<MmaduClient> loadClientByIdentifier(String clientIdentifier);
}
