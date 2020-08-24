package com.mmadu.identity.models.signin;

public class SignInProfile {
    private String title;
    private String message;
    private boolean showResetPassword;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isShowResetPassword() {
        return showResetPassword;
    }

    public void setShowResetPassword(boolean showResetPassword) {
        this.showResetPassword = showResetPassword;
    }
}
