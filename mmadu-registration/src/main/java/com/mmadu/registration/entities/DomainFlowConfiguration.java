package com.mmadu.registration.entities;

import com.mmadu.registration.models.PasswordResetFlowConfiguration;
import com.mmadu.registration.models.themes.ThemeConfiguration;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
public class DomainFlowConfiguration {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotEmpty(message = "domainId is required")
    private String domainId;
    private ThemeConfiguration theme = new ThemeConfiguration();
    private PasswordResetFlowConfiguration passwordReset = new PasswordResetFlowConfiguration();
}
