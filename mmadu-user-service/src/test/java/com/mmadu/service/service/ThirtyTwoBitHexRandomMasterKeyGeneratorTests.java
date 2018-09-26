package com.mmadu.service.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class ThirtyTwoBitHexRandomMasterKeyGeneratorTests {

    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    private ThirtyTwoBitHexRandomMasterKeyGenerator masterKeyGenerator;

    @Before
    public void setUp() {
        masterKeyGenerator = new ThirtyTwoBitHexRandomMasterKeyGenerator();
        masterKeyGenerator.setSeed(System.currentTimeMillis());
        masterKeyGenerator.initialize();
    }

    @Test
    public void testGeneratedMasterKey() {
        String masterKey = masterKeyGenerator.generateMasterKey();
        assertThat(masterKey, notNullValue());
        errorCollector.checkThat(masterKey.matches("[0-9a-f]+"), is(true));
        errorCollector.checkThat(masterKey.length(), is(equalTo(64)));
    }
}
