package com.mmadu.service.databaselisteners;

import com.mmadu.service.entities.AppUser;
import com.mmadu.service.utilities.AppUserPasswordHashUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class AppUserSaveListener extends AbstractMongoEventListener<AppUser> {
    @Autowired
    private AppUserPasswordHashUpdater passwordHashUpdater;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<AppUser> event) {
        AppUser user = event.getSource();
        String password = passwordHashUpdater.updatePasswordHash(user.getPassword());
        user.setPassword(password);
    }
}
