package com.mmadu.registration.services;

import com.mmadu.registration.entities.ValidationContext;
import com.mmadu.registration.exceptions.PropertyValidatorNotSupportedException;
import com.mmadu.registration.exceptions.PropertyValueExpectationFailedException;
import com.mmadu.registration.models.propertyvalidation.ValidationAttempt;
import com.mmadu.registration.models.propertyvalidation.ValidationRequest;
import com.mmadu.registration.providers.propertyvalidation.PropertyValidator;
import com.mmadu.registration.providers.propertyvalidation.PropertyValidatorResolver;
import com.mmadu.registration.repositories.ValidationContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class PropertyValidationServiceImpl implements PropertyValidationService {
    private PropertyValidatorResolver propertyValidatorResolver;
    private MmaduUserServiceClient userServiceClient;
    private ValidationContextRepository validationContextRepository;

    @Override
    public void initiateValidation(ValidationRequest request) {
        String property = userServiceClient.getUserProperty(request.getDomainId(), request.getUserId(), request.getPropertyName())
                .blockOptional()
                .orElseThrow(PropertyValueExpectationFailedException::new);
        validationContextRepository.deleteByDomainIdAndUserIdAndKey(
                request.getDomainId(), request.getUserId(), request.getKey()
        );
        ValidationContext context = new ValidationContext();
        context.setInitiatedTimestamp(ZonedDateTime.now());
        context.setDomainId(request.getDomainId());
        context.setUserId(request.getUserId());
        context.setKey(request.getKey());
        context.addAllData(request.getData());
        context.addAllData(getValidator(request.getValidationType()).prepareForValidation(request, property));
        validationContextRepository.save(context);
    }

    private PropertyValidator getValidator(String type) {
        return this.propertyValidatorResolver.getValidatorForType(type).orElseThrow(
                PropertyValidatorNotSupportedException::new
        );
    }

    @Override
    public boolean evaluateValidation(ValidationAttempt attempt) {
        Optional<ValidationContext> contextOptional =
                validationContextRepository.findByDomainIdAndUserIdAndKey(attempt.getDomainId(),
                        attempt.getUserId(), attempt.getKey());
        if (contextOptional.isEmpty()) {
            return false;
        }
        ValidationContext context = contextOptional.get();
        context.addAllData(attempt.getData());
        boolean valid = getValidator(attempt.getValidationType()).validate(attempt, context.getData());

        if (valid) {
            validationContextRepository.delete(context);
        } else {
            validationContextRepository.save(context);
        }
    }

    @Autowired
    public void setPropertyValidatorResolver(PropertyValidatorResolver propertyValidatorResolver) {
        this.propertyValidatorResolver = propertyValidatorResolver;
    }

    @Autowired
    public void setUserServiceClient(MmaduUserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Autowired
    public void setValidationContextRepository(ValidationContextRepository validationContextRepository) {
        this.validationContextRepository = validationContextRepository;
    }
}
