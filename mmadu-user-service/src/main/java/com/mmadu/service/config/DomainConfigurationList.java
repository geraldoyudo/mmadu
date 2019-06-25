package com.mmadu.service.config;

import com.mmadu.service.entities.AppDomain;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.domain-config")
@Data
public class DomainConfigurationList {
    private List<AppDomain> domains = new ArrayList<>();
}
