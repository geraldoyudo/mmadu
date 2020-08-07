package com.mmadu.notifications.service.provider;

import com.mmadu.notifications.endpoint.NotificationProvider;
import com.mmadu.notifications.endpoint.NotificationProviderRegistration;
import com.mmadu.notifications.endpoint.models.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
public class BeanNotificationProviderRegistry implements NotificationProviderRegistry {
    private Map<String, List<NotificationProvider>> registry = Collections.emptyMap();

    @PostConstruct
    public void init() {
        log.info("Loaded {} notification provider entries", registry.size());
    }

    @Autowired(required = false)
    public void setRegistrations(List<NotificationProviderRegistration> registrations) {
        registry = new HashMap<>();
        registrations.forEach(r -> {
            List<NotificationProvider> providers = getList(registry, r.getId());
            providers.add(r.getProvider());
        });
    }

    private static List<NotificationProvider> getList(Map<String, List<NotificationProvider>> providerMap, String id) {
        return providerMap.computeIfAbsent(id, k -> new LinkedList<>());
    }

    @Override
    public Optional<NotificationProvider> getProvider(String id, NotificationMessage message) {
        return Optional.ofNullable(registry.get(id)).orElse(Collections.emptyList())
                .stream()
                .filter(p -> p.supportsMessage(message))
                .findFirst();
    }
}
