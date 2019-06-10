package com.mmadu.service.providers;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UniqueUserIdGeneratorImpl implements UniqueUserIdGenerator {
    @Override
    public String generateUniqueId(String domainId) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
