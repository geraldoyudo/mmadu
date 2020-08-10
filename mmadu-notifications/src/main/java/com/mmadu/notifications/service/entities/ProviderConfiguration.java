package com.mmadu.notifications.service.entities;

import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Document
@EqualsAndHashCode
@CompoundIndexes({
        @CompoundIndex(name = "domain_profile_id_provider_id", def = "{'domainId': 1, 'providerId': 1, 'profileId': 1}", unique = true)
})
public class ProviderConfiguration implements NotificationProviderConfiguration {
    @Id
    private String id;
    private String domainId;
    private String profileId;
    private String providerId;
    private Map<String, Object> properties = new HashMap<>();

    public ProviderConfiguration() {
    }

    public ProviderConfiguration(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    @Override
    public <T> Optional<T> getProperty(String value) {
        return Optional.ofNullable(properties.get(value))
                .map(v -> (T) v);
    }
}
