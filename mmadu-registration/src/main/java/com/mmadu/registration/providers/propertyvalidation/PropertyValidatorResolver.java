package com.mmadu.registration.providers.propertyvalidation;

import java.util.Optional;

public interface PropertyValidatorResolver {

    Optional<PropertyValidator> getValidatorForType(String type);
}
