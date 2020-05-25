package com.mmadu.encryption;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ThirtyTwoBitHexRandomMasterKeyGeneratorTests {

    private ThirtyTwoBitHexRandomMasterKeyGenerator masterKeyGenerator;

    @BeforeEach
    public void setUp() {
        masterKeyGenerator = new ThirtyTwoBitHexRandomMasterKeyGenerator();
        masterKeyGenerator.setSeed(System.currentTimeMillis());
        masterKeyGenerator.initialize();
    }

    @Test
    public void testGeneratedMasterKey() {
        String masterKey = masterKeyGenerator.generateMasterKey();
        assertThat(masterKey, notNullValue());

        assertAll(
                () -> assertThat(masterKey.matches("[0-9a-f]+"), is(true)),
                () -> assertThat(masterKey.length(), is(equalTo(64)))
        );
    }
}
