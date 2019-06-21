package com.mmadu.registration.providers;

import com.mmadu.registration.models.UserForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

public class UserFormValidator implements Validator {
    private String domainId;
    private List<Validator> fieldTypeValidatorList;

    public UserFormValidator(String domainId, List<Validator> fieldTypeValidatorList) {
        this.domainId = domainId;
        this.fieldTypeValidatorList = fieldTypeValidatorList;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "properties['username']", "username.empty",
                new Object[]{}, "username property cannot be empty");
        UserForm form = (UserForm) o;
        fieldTypeValidatorList.forEach(
                validator -> validator.validate(form, errors)
        );
    }
}
