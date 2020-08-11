package com.mmadu.otp.service.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@Document
public class OtpToken {
    @Id
    private String id;
    private String key;
    private String profile;
    private String domainId;
    private String value;
    @Indexed(expireAfterSeconds = 3)
    private ZonedDateTime expiryTime;
}
