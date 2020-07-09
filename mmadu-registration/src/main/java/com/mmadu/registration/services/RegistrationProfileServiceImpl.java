package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.RegistrationProfileNotFoundException;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RegistrationProfileServiceImpl implements RegistrationProfileService {
    private RegistrationProfileRepository registrationProfileRepository;

    @Autowired
    public void setRegistrationProfileRepository(RegistrationProfileRepository registrationProfileRepository) {
        this.registrationProfileRepository = registrationProfileRepository;
    }

    @Override
    @Cacheable("registrationProfiles")
    public RegistrationProfile getProfileForDomainAndCode(String domainId, String code) {
        return registrationProfileRepository.findByDomainIdAndCode(domainId, code)
                .orElseThrow(RegistrationProfileNotFoundException::new);
    }
}
