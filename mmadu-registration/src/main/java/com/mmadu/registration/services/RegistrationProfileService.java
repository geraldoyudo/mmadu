package com.mmadu.registration.services;

import com.mmadu.registration.entities.RegistrationProfile;

public interface RegistrationProfileService {

    RegistrationProfile getProfileForDomain(String domainId);
}
