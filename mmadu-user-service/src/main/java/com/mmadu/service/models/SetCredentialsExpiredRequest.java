package com.mmadu.service.models;

public class SetCredentialsExpiredRequest {
    private boolean credentialExpired;

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }
}
