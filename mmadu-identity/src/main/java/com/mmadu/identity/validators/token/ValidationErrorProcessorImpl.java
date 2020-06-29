package com.mmadu.identity.validators.token;

import com.mmadu.identity.models.token.error.InvalidRequest;
import com.mmadu.identity.models.token.error.TokenError;
import com.mmadu.identity.validators.token.errorsstrategies.ValidationErrorProcessingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidationErrorProcessorImpl implements ValidationErrorProcessor {
    private List<ValidationErrorProcessingStrategy> strategies = Collections.emptyList();

    @Autowired(required = false)
    public void setStrategies(List<ValidationErrorProcessingStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public TokenError processError(BindingResult result) {
        for (FieldError error : result.getFieldErrors()) {
            for (ValidationErrorProcessingStrategy strategy : strategies) {
                if (strategy.apply(error.getField(), error.getCode())) {
                    return strategy.processError(error.getField(), error.getCode());
                }
            }
        }
        return new InvalidRequest(
                result.getFieldErrors().stream()
                        .map(error -> error.getCode())
                        .collect(Collectors.joining(","))
                , "");
    }
}
