package com.mmadu.service.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DefaultPropertiesMasterKeyResolver.class)
@TestPropertySource(properties = "mmadu.security.master-key=")
public class EmptyPropertyDefaultPropertiesMasterKeyResolverTest {
    @Rule
    public final OutputCapture outputCapture = new OutputCapture();
    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    @Autowired
    private MasterKeyResolver masterKeyResolver;

    @MockBean
    private MasterKeyGenerator masterKeyGenerator;

    @Before
    public void setUp(){
        doReturn("11111111111111111111111111111111").when(masterKeyGenerator).generateMasterKey();
    }

    @Test
    public void givenNoMasterKeyInPropertiesMasterKeyShouldBeGenerated() {
        String masterKey = masterKeyResolver.getMasterKey();
        errorCollector.checkThat(masterKeyResolver.getMasterKey(), notNullValue());
        errorCollector.checkThat(masterKeyResolver.getMasterKey(), is(not(equalTo(""))));
        errorCollector.checkThat(outputCapture.toString(), containsString(masterKey));
    }
}
