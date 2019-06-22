package com.mmadu.encryption;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MasterKeyCBCKeyCipherTest {
    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    private static final String KEY = "11111111111111111111111111111111";
    private static final String MASTER_KEY = "11111111111111111111111111111111";

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

    @Test
    public void encryptInstance(){
        String encryptedValue = ecbKeyCipher.encrypt("11111111111111111111111111111111");
        System.out.println(encryptedValue);
    }

}