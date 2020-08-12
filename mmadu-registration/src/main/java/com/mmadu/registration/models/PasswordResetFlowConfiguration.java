package com.mmadu.registration.models;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PasswordResetFlowConfiguration {
    private List<String> userFields = Collections.singletonList("username");
    private String initiationFormTitle;
    private String initiationFormDescription;
    private String userFieldPlaceholder;
    private String submitButtonLabel;
    private String initiationSuccessMessage;
    private String otpProfile = "password-reset";
    private String passwordConfirmationBaseUrl;
}
