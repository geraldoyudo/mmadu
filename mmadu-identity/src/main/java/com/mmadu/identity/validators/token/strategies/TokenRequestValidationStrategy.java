package com.mmadu.identity.validators.token.strategies;

import com.mmadu.identity.models.token.TokenRequest;
import org.springframework.validation.Errors;

public interface TokenRequestValidationStrategy {
    boolean apply(TokenRequest request);

    void validate(TokenRequest request, Errors errors);
}
