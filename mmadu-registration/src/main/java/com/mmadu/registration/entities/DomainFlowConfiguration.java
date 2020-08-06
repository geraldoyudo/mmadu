package com.mmadu.registration.entities;

import com.mmadu.registration.models.themes.ThemeConfiguration;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
public class DomainFlowConfiguration {
    @Id
    private String id;
    @NotEmpty(message = "domainId is required")
    private String domainId;
    private ThemeConfiguration theme = new ThemeConfiguration();
}
