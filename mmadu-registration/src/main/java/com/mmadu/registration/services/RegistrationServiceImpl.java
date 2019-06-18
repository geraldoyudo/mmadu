package com.mmadu.registration.services;

import com.mmadu.registration.models.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {
    @Override
    public void registerUser(UserForm userForm) {
        log.info("User: {}", userForm);
    }
}
