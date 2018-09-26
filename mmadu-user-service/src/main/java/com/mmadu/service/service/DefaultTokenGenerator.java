package com.mmadu.service.service;

import java.util.Random;
import javax.annotation.PostConstruct;
import org.springframework.security.crypto.codec.Hex;

public class DefaultTokenGenerator implements TokenGenerator {
    private static final int TOKEN_LENGTH = 128;
    private long seed = System.currentTimeMillis();
    private Random random;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    @PostConstruct
    public void initialize(){
        random = new Random(seed);
    }

    @Override
    public String generateToken() {
        if(random == null)
            throw new IllegalStateException("TokenGenerator not initialized!!");
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        random.nextBytes(tokenBytes);
        return new String(Hex.encode(tokenBytes));
    }
}
