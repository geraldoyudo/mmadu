package com.mmadu.registration.populators;

import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import com.mmadu.registration.repositories.RegistrationProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("populated")
class DomainFlowPopulatorTest {
    @Autowired
    private RegistrationProfileRepository registrationProfileRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldTypeRepository fieldTypeRepository;

    @Test
    void testConfigRead() throws Exception {
        Thread.sleep(2000);
        assertAll(
                () -> assertEquals(1, registrationProfileRepository.count()),
                () -> assertEquals(8, fieldTypeRepository.count()),
                () -> assertEquals(9, fieldRepository.count())
        );
    }
}