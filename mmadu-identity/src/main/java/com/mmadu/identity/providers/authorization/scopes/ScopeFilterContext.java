package com.mmadu.identity.providers.authorization.scopes;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
public class ScopeFilterContext {
    private DomainIdentityConfiguration configuration;
    private MmaduUser user;
    private MmaduClient client;
    private Map<String, Object> properties = new HashMap<>();


    public void set(String key, Object value) {
        properties.put(key, value);
    }

    public <T> Optional<T> get(String key) {
        return Optional.ofNullable(properties.get(key))
                .map(k -> (T) k);
    }
}
