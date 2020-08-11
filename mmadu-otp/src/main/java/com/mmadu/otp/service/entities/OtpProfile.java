package com.mmadu.otp.service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "otp_domain_profile", def = "{'domainId': 1, 'identifier': 1}", unique = true)
}
)
public class OtpProfile {
    @Id
    private String id;
    private String domainId;
    private String identifier;
}
