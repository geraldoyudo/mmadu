package com.mmadu.service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class RoleAuthority {
    @Id
    private String id;
    @DBRef
    private Role role;
    @DBRef
    private Authority authority;
}
