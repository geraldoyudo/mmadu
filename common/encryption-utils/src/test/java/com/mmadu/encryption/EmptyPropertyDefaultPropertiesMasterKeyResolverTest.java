package com.mmadu.encryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class EmptyPropertyDefaultPropertiesMasterKeyResolverTest {
    public static final String MASTER_KEY = "11111111111111111111111111111111";
    @InjectMocks
    private DefaultPropertiesMasterKeyResolver masterKeyResolver;

    @Mock
    private MasterKeyGenerator masterKeyGenerator;


    @BeforeEach
    public void setUp() {
        doReturn(MASTER_KEY).when(masterKeyGenerator).generateMasterKey();
    }

    @Test
    public void givenNoMasterKeyInPropertiesMasterKeyShouldBeGenerated() {
        String masterKey = masterKeyResolver.getMasterKey();
        assertEquals(masterKey, MASTER_KEY);
    }
}
