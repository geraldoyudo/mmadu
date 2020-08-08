package com.mmadu.notifications.service.services;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.entities.ProviderConfiguration;
import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.provider.NotificationProviderRegistry;
import com.mmadu.notifications.service.provider.NotificationProviderResolver;
import com.mmadu.notifications.service.provider.UserService;
import com.mmadu.notifications.service.repositories.ProviderConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = NotificationServiceImpl.class)
class NotificationServiceImplTest {
    public static final String DOMAIN_ID = "1";
    public static final String USER_ID = "2";
    public static final String USERNAME = "test";
    public static final String PROFILE_ID = "3";
    public static final String PROVIDER_1 = "provider1";
    public static final String MESSAGE_CONTENT = "content";
    public static final String MESSAGE_TEMPLATE = "template";
    @MockBean
    private UserService userService;
    @MockBean
    private NotificationProviderResolver providerResolver;
    @MockBean
    private NotificationProviderRegistry providerRegistry;
    @MockBean
    private ProviderConfigurationRepository providerConfigurationRepository;
    @Captor
    private ArgumentCaptor<NotificationMessage> messageCaptor;
    @Captor
    private ArgumentCaptor<NotificationProviderConfiguration> configurationCaptor;
    @MockBean
    private NotificationProvider provider;

    @Autowired
    private NotificationService notificationService;

    @Test
    void send() {
        when(userService.findByUserIdAndDomain(USER_ID, DOMAIN_ID)).thenReturn(Mono.just(user()));
        when(providerResolver.getProviderForMessage(any(), eq(DOMAIN_ID), eq(PROFILE_ID))).thenReturn(Mono.just(PROVIDER_1));
        when(providerRegistry.getProvider(eq(PROVIDER_1), any())).thenReturn(Optional.of(provider));
        when(providerConfigurationRepository.findByDomainIdAndProfileIdAndProviderId(DOMAIN_ID, PROFILE_ID, PROVIDER_1))
                .thenReturn(Optional.of(configuration()));
        AtomicBoolean providerCalled = new AtomicBoolean(false);
        when(provider.process(any(), any())).thenAnswer(iom -> {
            providerCalled.set(true);
            return Mono.empty();
        });

        notificationService.send(request()).block();

        verify(providerResolver, times(1)).getProviderForMessage(messageCaptor.capture(),
                eq(DOMAIN_ID), eq(PROFILE_ID));
        verify(providerRegistry, times(1)).getProvider(eq(PROVIDER_1), messageCaptor.capture());
        verify(provider).process(messageCaptor.capture(), configurationCaptor.capture());

        List<NotificationMessage> values = messageCaptor.getAllValues();
        NotificationMessage message = values.get(0);
        assertAll(
                () -> assertTrue(allValuesAreTheSame(values)),
                () -> assertEquals(user(), message.getContext().getUser().get()),
                () -> assertEquals(configuration(), configurationCaptor.getValue())
        );

    }

    private boolean allValuesAreTheSame(List<NotificationMessage> values) {
        return values.get(0).equals(values.get(1)) && values.get(1).equals(values.get(2))
                && values.get(2).equals(values.get(0));
    }

    private NotificationUser user() {
        NotificationUser user = new NotificationUser();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setProperty("country", "Nigeria");
        return user;
    }

    private ProviderConfiguration configuration() {
        ProviderConfiguration configuration = new ProviderConfiguration();
        configuration.setDomainId(DOMAIN_ID);
        configuration.setProfileId(PROFILE_ID);
        configuration.setProviderId(PROVIDER_1);
        return configuration;
    }

    private SendNotificationMessageRequest request() {
        SendNotificationMessageRequest request = new SendNotificationMessageRequest();
        request.setUserId(USER_ID);
        request.setContext(new HashMap<>());
        request.setHeaders(new HashMap<>());
        request.setDomainId(DOMAIN_ID);
        request.setProfileId(PROFILE_ID);
        request.setMessageContent(MESSAGE_CONTENT);
        request.setMessageTemplate(MESSAGE_TEMPLATE);
        return request;
    }
}