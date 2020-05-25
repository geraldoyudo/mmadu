package com.mmadu.tokenservice.providers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultTokenGeneratorTest {
    private DefaultTokenGenerator tokenGenerator;

    @BeforeEach
    void setUp() {
        tokenGenerator = new DefaultTokenGenerator();
        tokenGenerator.setSeed(0);
        tokenGenerator.initialize();
    }

    @Test
    void generateToken() {
        String token = tokenGenerator.generateToken();
        assertEquals(token, "60b420bb3851d9d47acb933dbe70399bf6c92da33af01d4fb770e98c0325f41d" +
                "3ebaf8986da712c82bcd4d554bf0b54023c29b624de9ef9c2f931efc580f9afb081b12e107b1e805f2b" +
                "4f5f0f1d00c2d0f62634670921c505867ff20f6a8335e98af8725385586b41feff205b4e05a000823f7" +
                "8b5f8f5c02439ce8f67a781d90");
    }
}