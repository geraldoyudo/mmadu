package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;

import java.util.List;

public interface RegistrationProfileService {

    RegistrationProfile getProfileForDomainAndCode(String domainId, String code);

    List<String> getAllProfileIds();
}
