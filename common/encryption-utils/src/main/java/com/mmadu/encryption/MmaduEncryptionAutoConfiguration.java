package com.mmadu.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MmaduEncryptionAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(value = "mmadu.domain.encrypt-keys", havingValue = "false", matchIfMissing = true)
    public static class NoOpConfiguration {
        @Bean
        public KeyCipher noOpKeyCipher() {
            return new NoOpKeyCipher();
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "mmadu.domain.encrypt-keys", havingValue = "true")
    public static class MainConfiguration {

        @Bean
        @ConditionalOnMissingBean(MasterKeyGenerator.class)
        public MasterKeyGenerator masterKeyGenerator(
                @Value("${mmadu.security.encryption-seed:0}") int encryptionSeed) {
            ThirtyTwoBitHexRandomMasterKeyGenerator generator = new ThirtyTwoBitHexRandomMasterKeyGenerator();
            generator.setSeed(encryptionSeed);
            return generator;
        }

        @Bean
        @ConditionalOnMissingBean(MasterKeyResolver.class)
        public MasterKeyResolver defaultPropertiesMasterKeyResolver(
                @Value("${mmadu.security.master-key:}") String masterKey,
                MasterKeyGenerator generator) {
            DefaultPropertiesMasterKeyResolver resolver = new DefaultPropertiesMasterKeyResolver();
            resolver.setDefaultMasterKey(masterKey);
            resolver.setMasterKeyGenerator(generator);
            return resolver;
        }

        @Bean
        @ConditionalOnMissingBean(KeyCipher.class)
        public KeyCipher masterKeyCBCKeyCipher(MasterKeyResolver masterKeyResolver) {
            MasterKeyCBCKeyCipher cipher = new MasterKeyCBCKeyCipher();
            cipher.setMasterKeyResolver(masterKeyResolver);
            return cipher;
        }
    }
}
