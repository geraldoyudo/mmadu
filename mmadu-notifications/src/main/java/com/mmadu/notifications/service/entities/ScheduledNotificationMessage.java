package com.mmadu.notifications.service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document
public class ScheduledNotificationMessage {
    @Id
    private String id;
    private String domainId;
    private String type;
    private List<String> eventTriggers = Collections.emptyList();
    private String userFilter;
    private String eventFilter;
    private String messageTemplate;
    private String message;
    private String profile = "default";
    private Map<String, Object> context = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();
}
