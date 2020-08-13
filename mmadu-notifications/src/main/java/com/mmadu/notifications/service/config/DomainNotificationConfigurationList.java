package com.mmadu.notifications.service.config;

import com.mmadu.notifications.service.entities.*;
import com.mmadu.notifications.service.models.NotificationRule;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "mmadu.domain-notification-config")
public class DomainNotificationConfigurationList {
    private List<DomainItem> domains;

    public List<DomainItem> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainItem> domains) {
        this.domains = domains;
    }

    public static class DomainItem {
        @NotEmpty
        private String domainId;
        @Size(min = 1)
        private List<NotificationProfileItem> profiles = Collections.emptyList();
        @Size(min = 1)
        private List<ProviderConfigurationItem> providerConfigurations = Collections.emptyList();
        private List<ScheduledUserNotificationMessageItem> userNotificationMessages = Collections.emptyList();
        private List<ScheduledEventNotificationMessageItem> eventNotificationMessages = Collections.emptyList();

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public List<NotificationProfileItem> getProfiles() {
            return profiles;
        }

        public void setProfiles(List<NotificationProfileItem> profiles) {
            this.profiles = profiles;
        }

        public List<ProviderConfigurationItem> getProviderConfigurations() {
            return providerConfigurations;
        }

        public void setProviderConfigurations(List<ProviderConfigurationItem> providerConfigurations) {
            this.providerConfigurations = providerConfigurations;
        }

        public List<ScheduledUserNotificationMessageItem> getUserNotificationMessages() {
            return userNotificationMessages;
        }

        public void setUserNotificationMessages(List<ScheduledUserNotificationMessageItem> userNotificationMessages) {
            this.userNotificationMessages = userNotificationMessages;
        }

        public List<ScheduledEventNotificationMessageItem> getEventNotificationMessages() {
            return eventNotificationMessages;
        }

        public void setEventNotificationMessages(List<ScheduledEventNotificationMessageItem> eventNotificationMessages) {
            this.eventNotificationMessages = eventNotificationMessages;
        }

        public DomainNotificationConfiguration toEntity() {
            DomainNotificationConfiguration configuration = new DomainNotificationConfiguration();
            configuration.setDomainId(domainId);
            return configuration;
        }
    }

    public static class NotificationProfileItem {
        @NotEmpty
        private String profileId;
        @Size(min = 1)
        private List<RuleItem> rules = Collections.emptyList();
        private List<ProviderConfigurationItem> providerConfigurations = Collections.emptyList();

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public List<RuleItem> getRules() {
            return rules;
        }

        public void setRules(List<RuleItem> rules) {
            this.rules = rules;
        }

        public List<ProviderConfigurationItem> getProviderConfigurations() {
            return providerConfigurations;
        }

        public void setProviderConfigurations(List<ProviderConfigurationItem> providerConfigurations) {
            this.providerConfigurations = providerConfigurations;
        }

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

    public static class RuleItem {
        private String expression;
        private String provider;
        private int priority;

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public NotificationRule toEntity() {
            return NotificationRule.builder()
                    .provider(provider)
                    .priority(priority)
                    .expression(expression)
                    .build();
        }
    }

    public static class ProviderConfigurationItem {
        private String profileId;
        @NotEmpty
        private String providerId;
        private Map<String, Object> properties = new HashMap<>();

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, Object> properties) {
            this.properties = properties;
        }

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

    public static class ScheduledUserNotificationMessageItem {
        @NotEmpty
        private String type;
        @Size(min = 1)
        private List<String> eventTriggers;
        private String userFilter;
        private String eventFilter;
        private String messageTemplate;
        private String message;
        private String profile = "default";
        private String destinationProperty;
        private Map<String, Object> context = Collections.emptyMap();
        private Map<String, Object> headers = Collections.emptyMap();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getEventTriggers() {
            return eventTriggers;
        }

        public void setEventTriggers(List<String> eventTriggers) {
            this.eventTriggers = eventTriggers;
        }

        public String getUserFilter() {
            return userFilter;
        }

        public void setUserFilter(String userFilter) {
            this.userFilter = userFilter;
        }

        public String getEventFilter() {
            return eventFilter;
        }

        public void setEventFilter(String eventFilter) {
            this.eventFilter = eventFilter;
        }

        public String getMessageTemplate() {
            return messageTemplate;
        }

        public void setMessageTemplate(String messageTemplate) {
            this.messageTemplate = messageTemplate;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDestinationProperty() {
            return destinationProperty;
        }

        public void setDestinationProperty(String destinationProperty) {
            this.destinationProperty = destinationProperty;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public Map<String, Object> getContext() {
            return context;
        }

        public void setContext(Map<String, Object> context) {
            this.context = context;
        }

        public Map<String, Object> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, Object> headers) {
            this.headers = headers;
        }

        public ScheduledUserNotificationMessage toEntity(String domainId) {
            ScheduledUserNotificationMessage notificationMessage = new ScheduledUserNotificationMessage();
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
            notificationMessage.setDestinationProperty(destinationProperty);
            return notificationMessage;
        }
    }

    public static class ScheduledEventNotificationMessageItem {
        @NotEmpty
        private String type;
        @Size(min = 1)
        private List<String> eventTriggers;
        private String eventFilter;
        private String messageTemplate;
        private String message;
        private String profile = "default";
        private String destinationExpression;
        private Map<String, Object> context = Collections.emptyMap();
        private Map<String, Object> headers = Collections.emptyMap();

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getEventTriggers() {
            return eventTriggers;
        }

        public void setEventTriggers(List<String> eventTriggers) {
            this.eventTriggers = eventTriggers;
        }

        public String getEventFilter() {
            return eventFilter;
        }

        public void setEventFilter(String eventFilter) {
            this.eventFilter = eventFilter;
        }

        public String getMessageTemplate() {
            return messageTemplate;
        }

        public void setMessageTemplate(String messageTemplate) {
            this.messageTemplate = messageTemplate;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getDestinationExpression() {
            return destinationExpression;
        }

        public void setDestinationExpression(String destinationExpression) {
            this.destinationExpression = destinationExpression;
        }

        public Map<String, Object> getContext() {
            return context;
        }

        public void setContext(Map<String, Object> context) {
            this.context = context;
        }

        public Map<String, Object> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, Object> headers) {
            this.headers = headers;
        }

        public ScheduledEventNotificationMessage toEntity(String domainId) {
            ScheduledEventNotificationMessage notificationMessage = new ScheduledEventNotificationMessage();
            notificationMessage.setDomainId(domainId);
            notificationMessage.setType(type);
            notificationMessage.setEventTriggers(eventTriggers);
            notificationMessage.setEventFilter(eventFilter);
            notificationMessage.setMessageTemplate(messageTemplate);
            notificationMessage.setMessage(message);
            notificationMessage.setProfile(profile);
            notificationMessage.setDestinationExpression(destinationExpression);
            notificationMessage.setContext(new HashMap<>(context));
            notificationMessage.setHeaders(new HashMap<>(headers));
            return notificationMessage;
        }
    }
}
