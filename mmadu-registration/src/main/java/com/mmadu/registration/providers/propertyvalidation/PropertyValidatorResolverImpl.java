package com.mmadu.registration.providers.propertyvalidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PropertyValidatorResolverImpl implements PropertyValidatorResolver {
    private Map<String, PropertyValidator> propertyValidatorMap = Collections.emptyMap();

    @Autowired(required = false)
    public void setPropertyValidators(List<PropertyValidator> propertyValidators) {
        propertyValidatorMap = propertyValidators.stream()
                .collect(Collectors.toMap(PropertyValidator::type, v -> v));
    }

    @Override
    public Optional<PropertyValidator> getValidatorForType(String type) {
        return Optional.ofNullable(propertyValidatorMap.get(type));
    }
}
