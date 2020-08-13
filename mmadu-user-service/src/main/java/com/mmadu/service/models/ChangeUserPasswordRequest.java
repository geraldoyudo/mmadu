package com.mmadu.service.models;

import javax.validation.constraints.NotEmpty;

public class ChangeUserPasswordRequest {
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
