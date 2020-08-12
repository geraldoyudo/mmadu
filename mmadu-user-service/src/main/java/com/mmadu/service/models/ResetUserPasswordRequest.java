package com.mmadu.service.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ResetUserPasswordRequest {
    @NotEmpty
    private String newPassword;
}
