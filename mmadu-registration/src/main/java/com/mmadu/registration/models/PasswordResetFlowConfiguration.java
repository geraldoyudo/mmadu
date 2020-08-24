package com.mmadu.registration.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.List;

public class PasswordResetFlowConfiguration {
    private List<String> userFields = Collections.singletonList("username");
    private String initiationFormTitle;
    private String initiationFormDescription;
    private String confirmationFormTitle;
    private String confirmationFormDescription;
    private String userFieldPlaceholder;
    private String submitButtonLabel;
    private String resourcesBaseUrl;
    private String formUrl;
    private String initiationSuccessMessage;
    private String confirmationSuccessMessage;
    private String otpProfile = "password-reset";
    private String passwordConfirmationBaseUrl;

    public List<String> getUserFields() {
        return userFields;
    }

    public void setUserFields(List<String> userFields) {
        this.userFields = userFields;
    }

    public String getInitiationFormTitle() {
        return initiationFormTitle;
    }

    public void setInitiationFormTitle(String initiationFormTitle) {
        this.initiationFormTitle = initiationFormTitle;
    }

    public String getInitiationFormDescription() {
        return initiationFormDescription;
    }

    public void setInitiationFormDescription(String initiationFormDescription) {
        this.initiationFormDescription = initiationFormDescription;
    }

    public String getConfirmationFormTitle() {
        return confirmationFormTitle;
    }

    public void setConfirmationFormTitle(String confirmationFormTitle) {
        this.confirmationFormTitle = confirmationFormTitle;
    }

    public String getConfirmationFormDescription() {
        return confirmationFormDescription;
    }

    public void setConfirmationFormDescription(String confirmationFormDescription) {
        this.confirmationFormDescription = confirmationFormDescription;
    }

    public String getUserFieldPlaceholder() {
        return userFieldPlaceholder;
    }

    public void setUserFieldPlaceholder(String userFieldPlaceholder) {
        this.userFieldPlaceholder = userFieldPlaceholder;
    }

    public String getSubmitButtonLabel() {
        return submitButtonLabel;
    }

    public void setSubmitButtonLabel(String submitButtonLabel) {
        this.submitButtonLabel = submitButtonLabel;
    }

    public String getResourcesBaseUrl() {
        return resourcesBaseUrl;
    }

    public void setResourcesBaseUrl(String resourcesBaseUrl) {
        this.resourcesBaseUrl = resourcesBaseUrl;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getInitiationSuccessMessage() {
        return initiationSuccessMessage;
    }

    public void setInitiationSuccessMessage(String initiationSuccessMessage) {
        this.initiationSuccessMessage = initiationSuccessMessage;
    }

    public String getConfirmationSuccessMessage() {
        return confirmationSuccessMessage;
    }

    public void setConfirmationSuccessMessage(String confirmationSuccessMessage) {
        this.confirmationSuccessMessage = confirmationSuccessMessage;
    }

    public String getOtpProfile() {
        return otpProfile;
    }

    public void setOtpProfile(String otpProfile) {
        this.otpProfile = otpProfile;
    }

    public String getPasswordConfirmationBaseUrl() {
        return passwordConfirmationBaseUrl;
    }

    public void setPasswordConfirmationBaseUrl(String passwordConfirmationBaseUrl) {
        this.passwordConfirmationBaseUrl = passwordConfirmationBaseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetFlowConfiguration that = (PasswordResetFlowConfiguration) o;

        return new EqualsBuilder()
                .append(userFields, that.userFields)
                .append(initiationFormTitle, that.initiationFormTitle)
                .append(initiationFormDescription, that.initiationFormDescription)
                .append(confirmationFormTitle, that.confirmationFormTitle)
                .append(confirmationFormDescription, that.confirmationFormDescription)
                .append(userFieldPlaceholder, that.userFieldPlaceholder)
                .append(submitButtonLabel, that.submitButtonLabel)
                .append(resourcesBaseUrl, that.resourcesBaseUrl)
                .append(formUrl, that.formUrl)
                .append(initiationSuccessMessage, that.initiationSuccessMessage)
                .append(confirmationSuccessMessage, that.confirmationSuccessMessage)
                .append(otpProfile, that.otpProfile)
                .append(passwordConfirmationBaseUrl, that.passwordConfirmationBaseUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userFields)
                .append(initiationFormTitle)
                .append(initiationFormDescription)
                .append(confirmationFormTitle)
                .append(confirmationFormDescription)
                .append(userFieldPlaceholder)
                .append(submitButtonLabel)
                .append(resourcesBaseUrl)
                .append(formUrl)
                .append(initiationSuccessMessage)
                .append(confirmationSuccessMessage)
                .append(otpProfile)
                .append(passwordConfirmationBaseUrl)
                .toHashCode();
    }
}
