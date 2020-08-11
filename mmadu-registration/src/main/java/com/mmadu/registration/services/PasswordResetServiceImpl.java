package com.mmadu.registration.services;

import com.mmadu.registration.models.PasswordResetRequestForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Override
    public void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm) {
        log.info("Resetting password for domain {} {}", domainId, requestForm);
    }
}
