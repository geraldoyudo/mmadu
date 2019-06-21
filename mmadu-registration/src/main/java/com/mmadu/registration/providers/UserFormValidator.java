package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.models.UserForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class UserFormValidator implements Validator {
    private String domainId;
    private List<Field> fieldList;

    public UserFormValidator(String domainId, List<Field> fieldList) {
        this.domainId = domainId;
        this.fieldList = fieldList;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    }
}
