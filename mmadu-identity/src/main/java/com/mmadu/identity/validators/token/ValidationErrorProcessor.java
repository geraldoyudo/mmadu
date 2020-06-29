package com.mmadu.identity.validators.token;

import com.mmadu.identity.models.token.error.TokenError;
import org.springframework.validation.BindingResult;

public interface ValidationErrorProcessor {
    TokenError processError(BindingResult result);
}
