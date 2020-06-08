package com.mmadu.service.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Data
@Document
@NoArgsConstructor
public class RoleAuthority {
    @Id
    private String id;
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @DBRef
    @NotEmpty(message = "role is required")
    private Role role;
    @DBRef
    @NotEmpty(message = "authority is required")
    private Authority authority;

    public RoleAuthority(@NotEmpty(message = "domainId is required") String domainId, @NotEmpty(message = "role is required") Role role, @NotEmpty(message = "authority is required") Authority authority) {
        this.domainId = domainId;
        this.role = role;
        this.authority = authority;
    }
}
