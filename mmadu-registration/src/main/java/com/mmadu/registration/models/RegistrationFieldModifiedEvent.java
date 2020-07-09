package com.mmadu.registration.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationFieldModifiedEvent {
    public static final String ALL_PROFILE = "###$$ALL__PROFILE$$##";

    private String profileId;

    public RegistrationFieldModifiedEvent() {
        profileId = ALL_PROFILE;
    }
}
