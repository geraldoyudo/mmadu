package com.mmadu.registration.providers;

import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.models.UserModel;

public interface UserFormConverter {

    UserModel convertToUserProperties(String domainId, UserForm userForm);
}
