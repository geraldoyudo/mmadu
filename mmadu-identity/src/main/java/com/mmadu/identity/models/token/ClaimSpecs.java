package com.mmadu.identity.models.token;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaimSpecs {
    private String type;
    private ClaimConfiguration configuration;
}
