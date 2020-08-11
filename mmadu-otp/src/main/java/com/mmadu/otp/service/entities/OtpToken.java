package com.mmadu.otp.service.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Document
public class OtpToken {
    private String id;
    private String key;
    private String profile;
    private String domainId;
    private String value;
    @Indexed(expireAfterSeconds = 3)
    private ZonedDateTime expiryTime;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean hasExpired() {
        if (expiryTime == null) {
            return false;
        } else {
            return ZonedDateTime.now().isAfter(expiryTime);
        }
    }
}
