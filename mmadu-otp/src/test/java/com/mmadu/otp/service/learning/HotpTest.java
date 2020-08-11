package com.mmadu.otp.service.learning;

import com.eatthepath.otp.HmacOneTimePasswordGenerator;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import java.security.Key;

public class HotpTest {

    @Test
    void testHotpGeneration() throws Exception {
        HmacOneTimePasswordGenerator generator = new HmacOneTimePasswordGenerator();
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(generator.getAlgorithm());
        keyGenerator.init(512);
        Key key = keyGenerator.generateKey();
        for (int i = 0; i < 10; ++i) {
            System.out.println(generator.generateOneTimePassword(key, i));
        }
    }
}
