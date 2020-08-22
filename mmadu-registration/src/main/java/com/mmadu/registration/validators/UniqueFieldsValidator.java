package com.mmadu.registration.validators;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.services.MmaduUserServiceClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mmadu.registration.utils.RegistrationRequestFields.FIELD_LIST;

@Component
public class UniqueFieldsValidator implements Validator {
    private HttpServletRequest request;
    private MmaduUserServiceClient userService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setUserService(MmaduUserServiceClient userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm form = (UserForm) o;
        List<Field> uniqueFields = Optional.ofNullable((List<Field>) request.getAttribute(FIELD_LIST)).orElse(Collections.emptyList())
                .stream()
                .filter(Field::isUnique)
                .collect(Collectors.toList());
        ensureUnique(form, uniqueFields, errors);
    }

    private void ensureUnique(UserForm form, List<Field> uniqueFields, Errors errors) {
        Optional<ImmutablePair<Field, Boolean>> result = Flux.fromIterable(uniqueFields)
                .flatMap(field -> userService.containsByQuery(field.getDomainId(), String.format("%s equals '%s'", field.getProperty(),
                        form.get(field.getProperty()).orElse("")))
                        .map(contains -> new ImmutablePair<Field, Boolean>(field, contains))
                )
                .filter(fieldContains -> fieldContains.getValue())
                .next()
                .blockOptional();
        result
                .map(Pair::getKey)
                .ifPresent(field ->
                errors.rejectValue(getProperty(field.getProperty()),
                        field.getProperty() + ".not.unique", field.getName() +" already exists"));
    }

    private String getProperty(String fieldProperty) {
        return String.format("properties[\"%s\"]", fieldProperty);
    }
}
