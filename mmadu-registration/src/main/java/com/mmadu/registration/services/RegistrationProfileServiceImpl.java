package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.exceptions.RegistrationProfileNotFoundException;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationProfileServiceImpl implements RegistrationProfileService {
    private RegistrationProfileRepository registrationProfileRepository;

    @Autowired
    public void setRegistrationProfileRepository(RegistrationProfileRepository registrationProfileRepository) {
        this.registrationProfileRepository = registrationProfileRepository;
    }

    @Override
    @Cacheable("registrationProfiles")
    @Transactional(readOnly = true)
    public RegistrationProfile getProfileForDomainAndCode(String domainId, String code) {
        return registrationProfileRepository.findByDomainIdAndCode(domainId, code)
                .orElseThrow(RegistrationProfileNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllProfileIds() {
        return registrationProfileRepository.findAll()
                .stream()
                .map(RegistrationProfile::getId)
                .collect(Collectors.toList());
    }
}
