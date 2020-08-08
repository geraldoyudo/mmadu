package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.service.entities.NotificationProfile;
import com.mmadu.notifications.service.models.NotificationRule;
import com.mmadu.notifications.service.repositories.NotificationProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NotificationProviderResolverImpl.class)
class NotificationProviderResolverImplTest {
    public static final String DOMAIN_ID = "1";
    public static final String PROFILE_ID = "2";
    public static final String EXPRESSION_1 = "expression-1";
    public static final String PROVIDER_1 = "provider1";
    public static final String EXPRESSION_2 = "expression-2";
    public static final String PROVIDER_2 = "provider2";
    public static final String EXPRESSION_3 = "expression-3";
    public static final String PROVIDER_3 = "provider3";
    @MockBean
    private NotificationProfileRepository notificationProfileRepository;
    @MockBean
    private NotificationRuleMatcher matcher;
    @MockBean
    private NotificationProfile notificationProfile;
    @MockBean
    private NotificationMessage message;

    @Autowired
    private NotificationProviderResolver providerResolver;

    @Test
    void getProviderForMessage() {
        when(notificationProfileRepository.findByDomainIdAndProfileId(DOMAIN_ID, PROFILE_ID))
                .thenReturn(Optional.of(notificationProfile));
        when(notificationProfile.getRules()).thenReturn(rules());
        when(matcher.matchesRule(EXPRESSION_1, message)).thenReturn(false);
        when(matcher.matchesRule(EXPRESSION_2, message)).thenReturn(true);
        when(matcher.matchesRule(EXPRESSION_3, message)).thenReturn(true);

        assertEquals(PROVIDER_3, providerResolver.getProviderForMessage(message, DOMAIN_ID, PROFILE_ID).block());
    }

    private List<NotificationRule> rules() {
        return asList(
                NotificationRule.builder()
                        .expression(EXPRESSION_1)
                        .priority(50)
                        .provider(PROVIDER_1)
                        .build(),
                NotificationRule.builder()
                        .expression(EXPRESSION_2)
                        .priority(100)
                        .provider(PROVIDER_2)
                        .build(),
                NotificationRule.builder()
                        .expression(EXPRESSION_3)
                        .priority(50)
                        .provider(PROVIDER_3)
                        .build()
        );
    }
}