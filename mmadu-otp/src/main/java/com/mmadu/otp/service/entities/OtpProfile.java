package com.mmadu.otp.service.entities;

import com.mmadu.otp.service.models.TimeToLive;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "otp_domain_profile", def = "{'domainId': 1, 'identifier': 1}", unique = true)
}
)
public class OtpProfile {
    @Id
    private String id;
    private String domainId;
    private String identifier;
    private TimeToLive otpValidity;
    private String type;
    private int otpLength;
    private Map<String, Object> configuration = new HashMap<>();

    public void setConfiguration(String key, Object property) {
        this.configuration.put(key, property);
    }

    public <T> Optional<T> getConfiguration(String key) {
        return Optional.ofNullable(configuration.get(key))
                .map(v -> (T) v);
    }
}
