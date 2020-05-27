package com.mmadu.encryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = MmaduEncryptionAutoConfiguration.MainConfiguration.class)
@TestPropertySource(properties = {
        "mmadu.security.master-key=11111111111111111111111111111111",
        "mmadu.domain.encrypt-keys=true"
})
class DefaultPropertiesMasterKeyResolverTest {

    @Autowired
    private MasterKeyResolver masterKeyResolver;

    @MockBean
    private MasterKeyGenerator masterKeyGenerator;

    @BeforeEach
    public void setUp() {
        doReturn("1232243434343434").when(masterKeyGenerator).generateMasterKey();
    }

    @Test
    public void givenMasterKeyInPropertiesMasterKeyShouldBeTheSame() {
        assertThat(masterKeyResolver.getMasterKey(), is(equalTo("11111111111111111111111111111111")));
    }
}
