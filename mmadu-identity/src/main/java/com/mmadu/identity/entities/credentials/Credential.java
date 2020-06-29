package com.mmadu.identity.entities.credentials;

import com.mmadu.identity.entities.HasDomain;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Credential implements HasDomain {
    private String id;
    private String domainId;
    private String type;
    private CredentialData data;

    public void setData(CredentialData data) {
        this.type = data.getType();
        this.data = data;
    }
}
