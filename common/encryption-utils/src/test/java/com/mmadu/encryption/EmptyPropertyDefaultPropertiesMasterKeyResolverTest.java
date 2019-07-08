package com.mmadu.encryption;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.rule.OutputCapture;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class EmptyPropertyDefaultPropertiesMasterKeyResolverTest {
    @Rule
    public final OutputCapture outputCapture = new OutputCapture();
    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    @InjectMocks
    private DefaultPropertiesMasterKeyResolver masterKeyResolver;

    @Mock
    private MasterKeyGenerator masterKeyGenerator;


    @Before
    public void setUp() {
        doReturn("11111111111111111111111111111111").when(masterKeyGenerator).generateMasterKey();
    }

    @Test
    public void givenNoMasterKeyInPropertiesMasterKeyShouldBeGenerated() {
        String masterKey = masterKeyResolver.getMasterKey();
        errorCollector.checkThat(masterKey, notNullValue());
        errorCollector.checkThat(masterKey, is(not(equalTo(""))));
        errorCollector.checkThat(outputCapture.toString(), containsString(masterKey));
    }
}
