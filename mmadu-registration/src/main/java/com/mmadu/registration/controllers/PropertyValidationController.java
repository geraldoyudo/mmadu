package com.mmadu.registration.controllers;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.models.propertyvalidation.ValidationResponse;
import com.mmadu.registration.services.PropertyValidationService;
import com.mmadu.registration.validators.PropertyValidationAttemptValidator;
import com.mmadu.registration.validators.PropertyValidationRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/propertyValidation")
public class PropertyValidationController {
    private PropertyValidationRequestValidator propertyValidationRequestValidator;
    private PropertyValidationAttemptValidator propertyValidationAttemptValidator;
    private PropertyValidationService propertyValidationService;

    @InitBinder
    public void setUpValidators(WebDataBinder binder) {
        binder.addValidators(
                propertyValidationRequestValidator,
                propertyValidationAttemptValidator
        );
    }

    @PostMapping("/request")
    @PreAuthorize("hasAuthority('validation.property.' + #request.propertyName + '.request')")
    public void initiateValidation(@RequestBody @Valid ValidationRequest request) {
        propertyValidationService.initiateValidation(request);
    }

    @PostMapping("/attempt")
    @PreAuthorize("hasAuthority('validation.property.' + #request.propertyName + '.attempt')")
    public ValidationResponse attemptValidation(@RequestBody @Valid ValidationAttempt attempt) {
        boolean valid = propertyValidationService.evaluateValidation(attempt);
        return new ValidationResponse(valid);
    }

    @Autowired
    public void setPropertyValidationAttemptValidator(PropertyValidationAttemptValidator propertyValidationAttemptValidator) {
        this.propertyValidationAttemptValidator = propertyValidationAttemptValidator;
    }

    @Autowired
    public void setPropertyValidationRequestValidator(PropertyValidationRequestValidator propertyValidationRequestValidator) {
        this.propertyValidationRequestValidator = propertyValidationRequestValidator;
    }

    @Autowired
    public void setPropertyValidationService(PropertyValidationService propertyValidationService) {
        this.propertyValidationService = propertyValidationService;
    }
}
