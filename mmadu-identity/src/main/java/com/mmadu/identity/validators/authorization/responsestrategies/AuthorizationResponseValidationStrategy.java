package com.mmadu.identity.validators.authorization.responsestrategies;

import com.mmadu.identity.models.authorization.AuthorizationResponse;
import org.springframework.validation.Errors;

public interface AuthorizationResponseValidationStrategy {

    boolean apply(AuthorizationResponse response);

    void validate(AuthorizationResponse response, Errors errors);
}
