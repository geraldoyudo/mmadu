package com.mmadu.identity.validators.authorization.requeststrategies;

import com.mmadu.identity.models.authorization.AuthorizationRequest;
import org.springframework.validation.Errors;

public interface AuthorizationRequestValidationStrategy {

    boolean apply(AuthorizationRequest request);

    void validate(AuthorizationRequest request, Errors errors);
}
