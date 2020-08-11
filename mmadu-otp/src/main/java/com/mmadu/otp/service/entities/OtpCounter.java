package com.mmadu.otp.service.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
public class OtpCounter {
    private String id;
    private String domainId;
    private String profile;
    private String type;
    private String key;
    private BigDecimal value = BigDecimal.ZERO;
}
