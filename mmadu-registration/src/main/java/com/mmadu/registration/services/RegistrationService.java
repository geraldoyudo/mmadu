package com.mmadu.registration.services;

import com.mmadu.registration.models.UserForm;

public interface RegistrationService {

    void registerUser(String domainId, UserForm userForm);
}
