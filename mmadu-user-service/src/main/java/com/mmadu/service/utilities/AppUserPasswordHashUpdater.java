package com.mmadu.service.utilities;

import com.mmadu.service.providers.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserPasswordHashUpdater {
    private PasswordHasher passwordHasher;

    @Autowired
    public void setPasswordHasher(PasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public String updatePasswordHash(String password) {
        if (passwordHasher.isEncoded(password)) {
            return password;
        } else {
            return passwordHasher.hashPassword(password);
        }
    }
}
