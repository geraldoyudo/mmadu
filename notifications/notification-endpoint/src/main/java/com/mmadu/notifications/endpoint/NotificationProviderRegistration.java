package com.mmadu.notifications.endpoint;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationProviderRegistration {
    private final String id;
    private final NotificationProvider provider;
}
