package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.UserFormValidationException;
import com.mmadu.registration.models.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private MmaduUserServiceClient userServiceClient;
    private RegistrationProfileService registrationProfileService;

    @Autowired
    public void setRegistrationProfileService(RegistrationProfileService registrationProfileService) {
        this.registrationProfileService = registrationProfileService;
    }

    @Autowired
    public void setUserServiceClient(MmaduUserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void registerUser(String domainId, UserForm userForm) {
        if (userForm == null) {
            throw new IllegalArgumentException("user form cannot be null");
        }
        if (StringUtils.isEmpty(userForm.get("username").orElse(""))) {
            throw new UserFormValidationException("username cannot be empty");
        }

        RegistrationProfile profile = registrationProfileService.getProfileForDomain(domainId);
        if (!userForm.get("roles").isPresent()) {
            userForm.set("roles", profile.getDefaultRoles());
        }
        if (!userForm.get("authorities").isPresent()) {
            userForm.set("authorities", profile.getDefaultAuthorities());
        }
        if (!userForm.get("password").isPresent()) {
            userForm.set("password", "");
        }
        userServiceClient.addUsers(domainId, userForm.getProperties());
    }
}
