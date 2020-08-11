package com.mmadu.notifications.service.services;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import com.mmadu.notifications.endpoint.models.NotificationProviderConfiguration;
import com.mmadu.notifications.endpoint.models.NotificationUser;
import com.mmadu.notifications.service.entities.ProviderConfiguration;
import com.mmadu.notifications.service.exceptions.ProviderNotFoundException;
import com.mmadu.notifications.service.models.DefaultNotificationHeaders;
import com.mmadu.notifications.service.models.DefaultNotificationMessage;
import com.mmadu.notifications.service.models.MapBasedNotificationContext;
import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.provider.NotificationProviderRegistry;
import com.mmadu.notifications.service.provider.NotificationProviderResolver;
import com.mmadu.notifications.service.provider.UserService;
import com.mmadu.notifications.service.repositories.ProviderConfigurationRepository;
import com.mmadu.notifications.service.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    public static final String DEFAULT_PROFILE = "default";
    private UserService userService;
    private NotificationProviderResolver notificationProviderResolver;
    private NotificationProviderRegistry notificationProviderRegistry;
    private ProviderConfigurationRepository providerConfigurationRepository;

    @Autowired
    public void setProviderConfigurationRepository(ProviderConfigurationRepository providerConfigurationRepository) {
        this.providerConfigurationRepository = providerConfigurationRepository;
    }

    @Autowired
    public void setNotificationProviderRegistry(NotificationProviderRegistry notificationProviderRegistry) {
        this.notificationProviderRegistry = notificationProviderRegistry;
    }

    @Autowired
    public void setNotificationProviderResolver(NotificationProviderResolver notificationProviderResolver) {
        this.notificationProviderResolver = notificationProviderResolver;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Void> send(SendNotificationMessageRequest request) {
        log.info("Sending message {}", request);
        return userService.findByUserIdAndDomain(request.getUserId(), request.getDomainId())
                .map(user -> prepareNotificationMessage(request, user))
                .flatMap(message -> this.sendMessage(message, request.getDomainId(), request.getProfileId()));
    }

    private NotificationMessage prepareNotificationMessage(SendNotificationMessageRequest request, NotificationUser user) {
        return DefaultNotificationMessage.builder()
                .id(request.getId())
                .context(new MapBasedNotificationContext(request.getContext(), user))
                .headers(new DefaultNotificationHeaders(request.getHeaders()))
                .message(request.getMessageContent())
                .messageTemplate(request.getMessageTemplate())
                .type(request.getType())
                .build();
    }

    private Mono<Void> sendMessage(NotificationMessage message, String domainId, String profileId) {
        return notificationProviderResolver.getProviderForMessage(message, domainId, profileId)
                .map(providerId ->
                        notificationProviderRegistry.getProvider(providerId, message)
                                .map(provider -> new Pair<>(providerId, provider))
                                .orElseThrow(ProviderNotFoundException::new)
                ).flatMap(providerPair -> this.sendMessageWithProvider(domainId, profileId,
                        providerPair.getFirst(), providerPair.getSecond(), message));
    }

    private Mono<Void> sendMessageWithProvider(String domainId, String profileId, String providerId,
                                               NotificationProvider provider,
                                               NotificationMessage message) {
        return Mono.fromCallable(() -> getNotificationProviderConfiguration(domainId, profileId, providerId))
                .flatMap(config -> provider.process(message, config));
    }

    private NotificationProviderConfiguration getNotificationProviderConfiguration(String domainId, String profileId, String providerId) {
        Optional<NotificationProviderConfiguration> configuration =
                providerConfigurationRepository.findByDomainIdAndProfileIdAndProviderId(domainId, profileId, providerId)
                        .map(c -> c);
        if (configuration.isEmpty()) {
            configuration = providerConfigurationRepository.findByDomainIdAndProfileIdAndProviderId(domainId, DEFAULT_PROFILE, providerId)
                    .map(c -> c);
        }
        return configuration.orElse(new ProviderConfiguration());
    }
}
