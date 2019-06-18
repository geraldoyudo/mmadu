package com.mmadu.registration.repositories;

import com.mmadu.registration.entities.RegistrationProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mmadu.registration.utils.EntityUtils.createRegistrationProfile;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RegistrationProfileRepositoryTest {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;

    @Test
    public void saveRegistrationProfile() {
        RegistrationProfile profile = createRegistrationProfile("1");
        registrationProfileRepository.save(profile);
    }

    @Test(expected = DuplicateKeyException.class)
    public void ensureOneDomainPerRegistrationProfile() {
        RegistrationProfile profile = createRegistrationProfile("1");
        RegistrationProfile profile1 = createRegistrationProfile("2");
        profile1.setDomainId(profile.getDomainId());
        registrationProfileRepository.save(profile);
        registrationProfileRepository.save(profile1);
    }
}