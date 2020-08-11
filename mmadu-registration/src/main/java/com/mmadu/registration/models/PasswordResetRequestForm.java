package com.mmadu.registration.models;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class PasswordResetRequestForm {
    @NotEmpty
    private String user;
}
