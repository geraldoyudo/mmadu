package com.mmadu.identity.validators.clientinstancestrategies;

import com.mmadu.identity.entities.ClientInstance;
import org.springframework.validation.Errors;

public interface ClientInstanceValidatingStrategy {

    boolean apply(ClientInstance instance);

    void validate(ClientInstance instance, Errors errors);
}
