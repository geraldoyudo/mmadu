package com.mmadu.notifications.service.config;

import com.mmadu.notifications.service.entities.DomainNotificationConfiguration;
import com.mmadu.notifications.service.entities.NotificationProfile;
import com.mmadu.notifications.service.entities.ProviderConfiguration;
import com.mmadu.notifications.service.entities.ScheduledNotificationMessage;
import com.mmadu.notifications.service.models.NotificationRule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ConfigurationProperties(prefix = "mmadu.domain-notification-config")
public class DomainNotificationConfigurationList {
    private List<DomainItem> domains;

    @Data
    public static class DomainItem {
        @NotEmpty
        private String domainId;
        @Size(min = 1)
        private List<NotificationProfileItem> profiles = Collections.emptyList();
        @Size(min = 1)
        private List<ProviderConfigurationItem> providerConfigurations = Collections.emptyList();
        private List<ScheduledNotificationMessageItem> notificationMessages = Collections.emptyList();

        public DomainNotificationConfiguration toEntity() {
            DomainNotificationConfiguration configuration = new DomainNotificationConfiguration();
            configuration.setDomainId(domainId);
            return configuration;
        }
    }

    @Data
    public static class NotificationProfileItem {
        @NotEmpty
        private String profileId;
        @Size(min = 1)
        private List<RuleItem> rules = Collections.emptyList();
        private List<ProviderConfigurationItem> providerConfigurations = Collections.emptyList();

        public NotificationProfile toEntity(String domainId) {
            NotificationProfile profile = new NotificationProfile();
            profile.setDomainId(domainId);
            profile.setProfileId(profileId);
            profile.setRules(
                    rules.stream()
                            .map(RuleItem::toEntity)
                            .sorted()
                            .collect(Collectors.toList())
            );
            return profile;
        }
    }

    @Data
    public static class RuleItem {
        private String expression;
        private String provider;
        private int priority;

        public NotificationRule toEntity() {
            return NotificationRule.builder()
                    .provider(provider)
                    .priority(priority)
                    .expression(expression)
                    .build();
        }
    }

    @Data
    public static class ProviderConfigurationItem {
        private String profileId;
        @NotEmpty
        private String providerId;
        private Map<String, Object> properties = new HashMap<>();

        public ProviderConfiguration toEntity(String domainId, String profileId) {
            ProviderConfiguration configuration = new ProviderConfiguration();
            configuration.setDomainId(domainId);
            configuration.setProfileId(profileId);
            configuration.setProviderId(providerId);
            configuration.setProperties(new HashMap<>(properties));
            return configuration;
        }

        public ProviderConfiguration toEntity(String domainId) {
            return toEntity(domainId, "default");
        }
    }

    @Data
    public static class ScheduledNotificationMessageItem {
        @NotEmpty
        private String type;
        @Size(min = 1)
        private List<String> eventTriggers;
        private String userFilter;
        private String eventFilter;
        private String messageTemplate;
        private String message;
        private String profile = "default";
        private Map<String, Object> context = Collections.emptyMap();
        private Map<String, Object> headers = Collections.emptyMap();

        public ScheduledNotificationMessage toEntity(String domainId) {
            ScheduledNotificationMessage notificationMessage = new ScheduledNotificationMessage();
            notificationMessage.setDomainId(domainId);
            notificationMessage.setType(type);
            notificationMessage.setEventTriggers(eventTriggers);
            notificationMessage.setUserFilter(userFilter);
            notificationMessage.setEventFilter(eventFilter);
            notificationMessage.setMessageTemplate(messageTemplate);
            notificationMessage.setMessage(message);
            notificationMessage.setProfile(profile);
            notificationMessage.setContext(new HashMap<>(context));
            notificationMessage.setHeaders(new HashMap<>(headers));
            return notificationMessage;
        }
    }
}
