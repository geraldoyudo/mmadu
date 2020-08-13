package com.mmadu.notifications.service.validators;

import com.mmadu.notifications.service.models.SendUserNotificationMessageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SendNotificationMessageRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return SendUserNotificationMessageRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SendUserNotificationMessageRequest request = (SendUserNotificationMessageRequest) o;
        if (StringUtils.isEmpty(request.getMessageContent()) && StringUtils.isEmpty(request.getMessageTemplate())) {
            errors.rejectValue("notification.message.required", "either message template or message is required");
        }
    }
}
