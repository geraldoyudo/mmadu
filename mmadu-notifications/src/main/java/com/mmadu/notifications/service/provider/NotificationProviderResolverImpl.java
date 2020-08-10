package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.service.exceptions.ProfileNotFoundException;
import com.mmadu.notifications.service.models.NotificationRule;
import com.mmadu.notifications.service.repositories.NotificationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class NotificationProviderResolverImpl implements NotificationProviderResolver {
    private NotificationProfileRepository notificationProfileRepository;
    private NotificationRuleMatcher ruleMatcher;

    @Autowired
    public void setNotificationProfileRepository(NotificationProfileRepository notificationProfileRepository) {
        this.notificationProfileRepository = notificationProfileRepository;
    }

    @Autowired
    public void setRuleMatcher(NotificationRuleMatcher ruleMatcher) {
        this.ruleMatcher = ruleMatcher;
    }

    @Override
    public Mono<String> getProviderForMessage(NotificationMessage message, String domainId, String profileId) {
        return Mono.fromCallable(() ->
                notificationProfileRepository.findByDomainIdAndProfileId(domainId, profileId)
                        .orElseThrow(ProfileNotFoundException::new)
        ).map(profile -> this.resolveFromRules(profile.getRules(), message));
    }

    private String resolveFromRules(List<NotificationRule> rules, NotificationMessage message) {
        return rules.stream().sorted()
                .filter(rule -> ruleMatcher.matchesRule(rule.getExpression(), message))
                .findFirst()
                .orElseThrow(ProfileNotFoundException::new)
                .getProvider();
    }
}
