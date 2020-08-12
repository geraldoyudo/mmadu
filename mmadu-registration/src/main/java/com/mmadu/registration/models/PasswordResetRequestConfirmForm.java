package com.mmadu.registration.models;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ToString
public class PasswordResetRequestConfirmForm {
    @NotEmpty
    private String userId;
    @NotEmpty
    private String otpId;
    @NotEmpty
    private String otpValue;
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
