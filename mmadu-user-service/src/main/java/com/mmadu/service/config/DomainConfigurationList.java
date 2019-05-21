package com.mmadu.service.config;

import com.mmadu.service.models.DomainConfig;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-config")
@Data
public class DomainConfigurationList {
    private List<DomainConfig> domains = new ArrayList<>();
}
