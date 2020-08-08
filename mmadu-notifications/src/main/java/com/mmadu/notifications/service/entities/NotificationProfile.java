package com.mmadu.notifications.service.entities;

import com.mmadu.notifications.service.models.NotificationRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@EqualsAndHashCode
@CompoundIndexes({
        @CompoundIndex(name = "domain_profile_id", def = "{'domainId': 1, 'profileId': 1}", unique = true)
})
public class NotificationProfile {
    private String domainId;
    private String profileId;
    private List<NotificationRule> rules;
}
