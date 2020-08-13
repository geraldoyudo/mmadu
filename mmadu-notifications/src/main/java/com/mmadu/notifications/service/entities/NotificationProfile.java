package com.mmadu.notifications.service.entities;

import com.mmadu.notifications.service.models.NotificationRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@EqualsAndHashCode
@CompoundIndexes({
        @CompoundIndex(name = "domain_profile_id", def = "{'domainId': 1, 'profileId': 1}", unique = true)
})
public class NotificationProfile {
    @Id
    private String id;
    private String domainId;
    private String profileId;
    private List<NotificationRule> rules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public List<NotificationRule> getRules() {
        return rules;
    }

    public void setRules(List<NotificationRule> rules) {
        this.rules = rules;
    }
}
