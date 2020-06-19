package com.mmadu.identity.learning;

import com.mmadu.identity.utilities.TokenGeneratorUtils;
import org.junit.jupiter.api.Test;

public class TokenGenerationTest {

    @Test
    void generateToken() throws Exception {
        System.out.println(TokenGeneratorUtils.getInstance().generateTokenWithAuthorities("a.*.**", "r.*.**"));
    }
}
