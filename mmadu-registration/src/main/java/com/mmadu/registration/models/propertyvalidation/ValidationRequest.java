package com.mmadu.registration.models.propertyvalidation;

import com.mmadu.security.api.DomainPayload;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValidationRequest implements DomainPayload {
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "userId is required")
    private String userId;
    @NotEmpty(message = "key is required")
    private String key;
    @NotEmpty(message = "propertyName is required")
    private String propertyName;
    @NotEmpty(message = "validationType is required")
    private String validationType;
    private Map<String, Object> data = new HashMap<>();

    @Override
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.ofNullable(data.get(key))
                .map(clazz::cast);
    }

    public void set(String key, Object value) {
        this.data.put(key, value);
    }
}
