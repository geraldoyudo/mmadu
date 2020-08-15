package com.mmadu.registration.entities;

import com.mmadu.registration.models.PasswordResetFlowConfiguration;
import com.mmadu.registration.models.PropertyValidationConfiguration;
import com.mmadu.registration.models.themes.ThemeConfiguration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@Document
public class DomainFlowConfiguration {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "domainId is required")
    private String domainId;
    private String jwkSetUri;
    private ThemeConfiguration theme = new ThemeConfiguration();
    private PasswordResetFlowConfiguration passwordReset = new PasswordResetFlowConfiguration();
    private Map<String, PropertyValidationConfiguration> propertyValidation = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public ThemeConfiguration getTheme() {
        return theme;
    }

    public void setTheme(ThemeConfiguration theme) {
        this.theme = theme;
    }

    public PasswordResetFlowConfiguration getPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(PasswordResetFlowConfiguration passwordReset) {
        this.passwordReset = passwordReset;
    }

    public Map<String, PropertyValidationConfiguration> getPropertyValidation() {
        return propertyValidation;
    }

    public void setPropertyValidation(Map<String, PropertyValidationConfiguration> propertyValidation) {
        this.propertyValidation = propertyValidation;
    }
}
