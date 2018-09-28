package com.mmadu.service.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MasterKeyCBCKeyCipherTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String KEY = "1111111111111111111111111111111111111111111111111111111111111111";
    private static final String MASTER_KEY = "2222222222222222222222222222222222222222222222222222222222222222";

    private MasterKeyCBCKeyCipher ecbKeyCipher;
    @Mock
    private MasterKeyResolver masterKeyResolver;

    @Before
    public void setUp(){
        ecbKeyCipher = new MasterKeyCBCKeyCipher();
        ecbKeyCipher.setMasterKeyResolver(masterKeyResolver);
        doReturn(MASTER_KEY).when(masterKeyResolver).getMasterKey();
        ecbKeyCipher.initialize();
    }

    @Test
    public void encrypt() {
        String encryptedValue = ecbKeyCipher.encrypt(KEY);
        collector.checkThat(encryptedValue, notNullValue());
        collector.checkThat(encryptedValue, not(equalTo(KEY)));
        String decryptedValue = ecbKeyCipher.decrypt(encryptedValue);
        collector.checkThat(decryptedValue, notNullValue());
        collector.checkThat(decryptedValue, equalTo(KEY));
    }

}