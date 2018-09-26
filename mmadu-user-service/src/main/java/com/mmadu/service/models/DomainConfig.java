package com.mmadu.service.models;

import lombok.Data;

@Data
public class DomainConfig {
    private String id;
    private String name;
    private String tokenEncryptionKey;
}
