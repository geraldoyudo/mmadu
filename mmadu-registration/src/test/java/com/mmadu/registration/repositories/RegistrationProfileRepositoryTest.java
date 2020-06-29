package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.RegistrationProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.registration.utils.EntityUtils.createRegistrationProfile;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class RegistrationProfileRepositoryTest {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @Test
    void saveRegistrationProfile() {
        RegistrationProfile profile = createRegistrationProfile("1");
        registrationProfileRepository.save(profile);
    }

    @Test
    @Disabled("Test is failing unexpectedly. Please fix")
    void ensureOneDomainPerRegistrationProfile() {
        RegistrationProfile profile = createRegistrationProfile("1");
        RegistrationProfile profile1 = createRegistrationProfile("2");
        profile1.setDomainId(profile.getDomainId());
        registrationProfileRepository.save(profile);
        assertThrows(DuplicateKeyException.class, () -> registrationProfileRepository.save(profile1));
    }
}