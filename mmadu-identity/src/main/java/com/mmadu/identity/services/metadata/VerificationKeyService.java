package com.mmadu.identity.services.metadata;

import java.util.Map;

public interface VerificationKeyService {

    Map<String, Object> getKeys(String domainId);
}
