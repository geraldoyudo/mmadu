package com.mmadu.service.entities;

import com.mmadu.security.api.DomainPayload;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserAuthority implements DomainPayload {
    @Id
    private String id;
    private String domainId;
    @DBRef
    private AppUser user;
    @DBRef
    private Authority authority;
}
