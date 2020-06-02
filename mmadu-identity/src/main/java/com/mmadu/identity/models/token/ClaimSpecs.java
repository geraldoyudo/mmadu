package com.mmadu.identity.models.token;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ClaimSpecs {
    private String type;
    @Builder.Default
    private ClaimConfiguration configuration = new ClaimConfiguration();
    @Builder.Default
    private String id = UUID.randomUUID().toString();
}
