package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationProfileServiceImpl implements RegistrationProfileService {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @Override
    public RegistrationProfile getProfileForDomain(String domainId) {
        return registrationProfileRepository.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
    }
}
