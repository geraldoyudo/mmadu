package com.mmadu.notifications.service.controllers;

import com.mmadu.notifications.service.models.SendNotificationMessageRequest;
import com.mmadu.notifications.service.services.NotificationService;
import com.mmadu.notifications.service.validators.SendNotificationMessageRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private NotificationService notificationService;
    private SendNotificationMessageRequestValidator requestValidator;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @InitBinder
    public void configureValidators(WebDataBinder binder) {
        binder.addValidators(requestValidator);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('notification.send')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> send(@RequestBody @Valid SendNotificationMessageRequest request) {
        return this.notificationService.send(request);
    }
}
