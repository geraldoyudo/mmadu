package com.mmadu.registration.services;

import com.mmadu.registration.models.PasswordResetRequestForm;

public interface PasswordResetService {

    void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm);
}
