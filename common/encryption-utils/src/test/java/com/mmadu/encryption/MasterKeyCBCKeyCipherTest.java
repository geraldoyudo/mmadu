package com.mmadu.encryption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MasterKeyCBCKeyCipherTest {

    private static final String KEY = "11111111111111111111111111111111";
    private static final String MASTER_KEY = "11111111111111111111111111111111";

    private MasterKeyCBCKeyCipher ecbKeyCipher;
    @Mock
    private MasterKeyResolver masterKeyResolver;

    @BeforeEach
    public void setUp() {
        ecbKeyCipher = new MasterKeyCBCKeyCipher();
        ecbKeyCipher.setMasterKeyResolver(masterKeyResolver);
        doReturn(MASTER_KEY).when(masterKeyResolver).getMasterKey();
        ecbKeyCipher.initialize();
    }

    @Test
    public void encrypt() {
        String encryptedValue = ecbKeyCipher.encrypt(KEY);
        String decryptedValue = ecbKeyCipher.decrypt(encryptedValue);

        assertAll(
                () -> assertThat(encryptedValue, notNullValue()),
                () -> assertThat(encryptedValue, not(equalTo(KEY))),
                () -> assertThat(decryptedValue, notNullValue()),
                () -> assertThat(decryptedValue, equalTo(KEY))
        );
    }

    @Test
    public void encryptInstance() {
        String encryptedValue = ecbKeyCipher.encrypt("11111111111111111111111111111111");
        System.out.println(encryptedValue);
    }

}