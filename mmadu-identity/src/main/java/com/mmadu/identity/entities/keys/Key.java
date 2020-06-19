package com.mmadu.identity.entities.keys;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Key {
    @Id
    private String id;
    private byte[] value;
}
