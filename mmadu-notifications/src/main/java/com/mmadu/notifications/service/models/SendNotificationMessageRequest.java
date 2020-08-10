package com.mmadu.notifications.service.models;

import com.mmadu.security.api.DomainPayload;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Data
public class SendNotificationMessageRequest implements DomainPayload {
    private String id = UUID.randomUUID().toString();
    @NotEmpty
    private String domainId;
    @NotEmpty
    private String profileId = "default";
    @NotEmpty
    private String userId;
    @NotEmpty
    private String type;
    private String messageTemplate;
    private String messageContent;
    private Map<String, Object> headers = Collections.emptyMap();
    private Map<String, Object> context = Collections.emptyMap();
}
