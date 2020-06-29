package com.mmadu.identity.validators.token.errorsstrategies;

import com.mmadu.identity.models.token.error.TokenError;

public interface ValidationErrorProcessingStrategy {

    boolean apply(String field, String fieldErrorCode);

    TokenError processError(String field, String fieldErrorCode);
}
