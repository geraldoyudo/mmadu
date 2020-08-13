package com.mmadu.registration.controllers;

import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.models.propertyvalidation.ValidationResponse;
import com.mmadu.registration.services.PropertyValidationService;
import com.mmadu.registration.validators.PropertyValidationAttemptValidator;
import com.mmadu.registration.validators.PropertyValidationRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/propertyValidation")
public class PropertyValidationController {
    private PropertyValidationRequestValidator propertyValidationRequestValidator;
    private PropertyValidationAttemptValidator propertyValidationAttemptValidator;
    private PropertyValidationService propertyValidationService;


    @PostMapping("/request")
    @PreAuthorize("hasAuthority('validation.property.' + #request.propertyName + '.request') || " +
            "((@currentUser != null) && (#request.userId == @currentUser.userId))")
    public void initiateValidation(@RequestBody @Valid ValidationRequest request, BindingResult result) {
        propertyValidationRequestValidator.validate(request, result);
        propertyValidationService.initiateValidation(request);
    }

    @PostMapping("/attempt")
    @PreAuthorize("hasAuthority('validation.property.' + #attempt.propertyName + '.attempt') || " +
            "((@currentUser != null) && (#attempt.userId == @currentUser.userId))")
    public ValidationResponse attemptValidation(@RequestBody @Valid ValidationAttempt attempt, BindingResult result) {
        propertyValidationAttemptValidator.validate(attempt, result);
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
