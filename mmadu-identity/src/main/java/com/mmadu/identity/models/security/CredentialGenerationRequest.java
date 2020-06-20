package com.mmadu.identity.models.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.Map;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CredentialGenerationRequest {
    @NotEmpty(message = "type is required")
    private String type;
    private Map<String, Object> properties = Collections.emptyMap();
}
