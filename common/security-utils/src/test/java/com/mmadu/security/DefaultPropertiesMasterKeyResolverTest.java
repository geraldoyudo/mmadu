package com.mmadu.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MmaduSecurityAutoConfiguration.MainConfiguration.class)
@TestPropertySource(properties = {
        "mmadu.security.master-key=11111111111111111111111111111111",
        "mmadu.domain.encrypt-keys=true"
})
public class DefaultPropertiesMasterKeyResolverTest {

    @Autowired
    private MasterKeyResolver masterKeyResolver;

    @MockBean
    private MasterKeyGenerator masterKeyGenerator;

    @Before
    public void setUp() {
        doReturn("1232243434343434").when(masterKeyGenerator).generateMasterKey();
    }

    @Test
    public void givenMasterKeyInPropertiesMasterKeyShouldBeTheSame() {
        assertThat(masterKeyResolver.getMasterKey(), is(equalTo("11111111111111111111111111111111")));
    }
}
