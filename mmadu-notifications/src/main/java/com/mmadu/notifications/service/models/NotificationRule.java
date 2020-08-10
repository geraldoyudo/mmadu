package com.mmadu.notifications.service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRule implements Comparable<NotificationRule> {
    private String expression;
    private String provider;
    private int priority = 100;

    @Override
    public int compareTo(NotificationRule o) {
        return Integer.compare(priority, o.getPriority());
    }
}
