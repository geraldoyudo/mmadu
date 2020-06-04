package com.mmadu.identity.providers.authorization.code;

import java.util.Map;

public interface AuthorizationCodeGenerationProvider {
    String type();

    String generateCode(Map<String, Object> properties);
}
