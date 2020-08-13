package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.UserFormValidationException;
import com.mmadu.registration.models.UserForm;
import com.mmadu.registration.models.UserModel;
import com.mmadu.registration.models.registration.DefaultAccountStatus;
import com.mmadu.registration.providers.UserFormConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    private MmaduUserServiceClient userServiceClient;
    private RegistrationProfileService registrationProfileService;
    private UserFormConverter userFormConverter;

    @Autowired
    public void setRegistrationProfileService(RegistrationProfileService registrationProfileService) {
        this.registrationProfileService = registrationProfileService;
    }

    @Autowired
    public void setUserServiceClient(MmaduUserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Autowired
    public void setUserFormConverter(UserFormConverter userFormConverter) {
        this.userFormConverter = userFormConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public void registerUser(String domainId, String code, UserForm userForm) {
        if (userForm == null) {
            throw new IllegalArgumentException("user form cannot be null");
        }
        if (StringUtils.isEmpty(userForm.get("username").orElse(""))) {
            throw new UserFormValidationException("username cannot be empty");
        }

        RegistrationProfile profile = registrationProfileService.getProfileForDomainAndCode(domainId, code);
        UserModel model = userFormConverter.convertToUserProperties(domainId, userForm);
        if (!model.get("roles").isPresent()) {
            model.set("roles", profile.getDefaultRoles());
        }
        if (!model.get("authorities").isPresent()) {
            model.set("authorities", profile.getDefaultAuthorities());
        }
        if (!model.get("groups").isPresent()) {
            model.set("groups", profile.getDefaultGroups());
        }
        if (!model.get("password").isPresent()) {
            model.set("password", "");
        }

        DefaultAccountStatus status = Optional.ofNullable(profile.getDefaultAccountStatus()).orElse(new DefaultAccountStatus());
        model.set("active", status.isActive());
        model.set("locked", status.isLocked());
        model.set("enabled", status.isEnabled());
        model.set("credentialExpired", status.isCredentialExpired());

        userServiceClient.addUsers(domainId, model.getProperties());
    }
}
