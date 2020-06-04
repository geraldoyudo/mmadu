package com.mmadu.identity.validators.credentials;

import com.mmadu.identity.models.security.CredentialGenerationRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RSACredentialRequestValidatingStrategy implements CredentialRequestValidatingStrategy {
    @Override
    public boolean apply(CredentialGenerationRequest request) {
        return "rsa".equals(request.getType());
    }

    @Override
    public void validate(CredentialGenerationRequest request, Errors errors) {
        // do nothing for now
    }
}
