package com.mmadu.service.config;

import com.mmadu.service.entities.AppToken;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mmadu.token-config")
@Data
public class TokenConfigurationList {

    private List<AppToken> tokens = new ArrayList<>();
}
