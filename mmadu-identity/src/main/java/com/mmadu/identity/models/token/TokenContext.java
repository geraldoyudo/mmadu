package com.mmadu.identity.models.token;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.models.client.MmaduClient;
import com.mmadu.identity.models.user.MmaduUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenContext {
    private MmaduClient client;
    private DomainIdentityConfiguration configuration;
}
