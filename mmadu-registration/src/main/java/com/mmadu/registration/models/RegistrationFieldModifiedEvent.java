package com.mmadu.registration.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationFieldModifiedEvent {
    public static final String ALL_DOMAIN = "###$$ALL__DOMAIN$$##";

    private String domain;

    public RegistrationFieldModifiedEvent() {
        domain = ALL_DOMAIN;
    }
}
