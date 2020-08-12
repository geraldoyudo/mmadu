package com.mmadu.registration.models.otp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Otp {
    private String id;
    private String value;
}
