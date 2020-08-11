package com.mmadu.registration.models;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PasswordResetFlowConfiguration {
    private List<UserField> userFields = Collections.singletonList(UserField.builder().label("username").name("username").build());
    private String initiationFormTitle;
    private String initiationFormDescription;
    private String userFieldPlaceholder;
    private String submitButtonLabel;
    private String initiationSuccessMessage;

    @Data
    @Builder
    public static class UserField {
        private String name;
        private String label;
    }
}
