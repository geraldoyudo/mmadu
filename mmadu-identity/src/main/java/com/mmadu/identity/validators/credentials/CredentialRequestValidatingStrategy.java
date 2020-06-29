package com.mmadu.identity.validators.credentials;

import com.mmadu.identity.models.security.CredentialGenerationRequest;
import org.springframework.validation.Errors;

public interface CredentialRequestValidatingStrategy {

    boolean apply(CredentialGenerationRequest request);

    void validate(CredentialGenerationRequest request, Errors errors);
}
