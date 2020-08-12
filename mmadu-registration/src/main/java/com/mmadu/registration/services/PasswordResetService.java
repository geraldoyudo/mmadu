package com.mmadu.registration.services;

import com.mmadu.registration.models.PasswordResetRequestConfirmForm;
import com.mmadu.registration.models.PasswordResetRequestForm;

public interface PasswordResetService {

    void initiatePasswordReset(String domainId, PasswordResetRequestForm requestForm);

    void confirmPasswordReset(String domainId, PasswordResetRequestConfirmForm requestForm);
}
