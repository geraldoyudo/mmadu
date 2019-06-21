package com.mmadu.registration.populators;

import com.mmadu.registration.config.ProfileConfigurationList;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfigurationProfilePopulator implements Populator {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;
    @Autowired
    private ProfileConfigurationList profileConfigurationList;

    @Override
    @PostConstruct
    public void populate() {
        if (registrationProfileRepository.count() > 0) {
            return;
        }
        registrationProfileRepository.saveAll(profileConfigurationList.getProfiles());
    }
}
