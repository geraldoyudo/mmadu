package com.mmadu.registration.services;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;

public interface PropertyValidationService {

    void initiateValidation(ValidationRequest request);

    boolean evaluateValidation(ValidationAttempt attempt);
}
