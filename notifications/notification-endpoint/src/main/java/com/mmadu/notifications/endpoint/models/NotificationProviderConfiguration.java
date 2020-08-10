package com.mmadu.notifications.endpoint.models;

import java.util.Optional;

@FunctionalInterface
public interface NotificationProviderConfiguration {

    <T> Optional<T> getProperty(String key);
}
