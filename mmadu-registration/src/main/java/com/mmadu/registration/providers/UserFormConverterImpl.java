package com.mmadu.registration.providers;

import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.models.UserModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserFormConverterImpl implements UserFormConverter {
    @Override
    public UserModel convertToUserProperties(String domainId, UserForm userForm) {
        Map<String, Object> returnValue = new HashMap<>();
        userForm.getProperties().entrySet()
                .stream()
                .forEach(entry -> returnValue.put(entry.getKey(), entry.getValue()));
        return new UserModel(returnValue);

    }
}
