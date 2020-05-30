package com.mmadu.identity.providers.client;

import java.util.Optional;

public interface MmaduClientService {

    Optional<MmaduClient> loadClientByIdentifier(String clientIdentifier);
}
