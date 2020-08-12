package com.mmadu.otp.service.config;

import com.mmadu.otp.service.entities.DomainOtpConfiguration;
import com.mmadu.otp.service.entities.OtpProfile;
import com.mmadu.otp.service.models.TimeToLive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "mmadu.domain-otp-config")
public class DomainOtpConfigurationList {
    private List<DomainItem> domains = Collections.emptyList();

    @Data
    public static class DomainItem {
        @NotEmpty
        private String domainId;
        @Size(min = 1)
        private List<String> supportedProviders = Collections.singletonList("alphanumeric");
        @Size(min = 1)
        private List<OtpProfileItem> profiles;

        public DomainOtpConfiguration toEntity() {
            DomainOtpConfiguration configuration = new DomainOtpConfiguration();
            configuration.setDomainId(domainId);
            configuration.setSupportedProviders(supportedProviders);
            return configuration;
        }
    }

    @Data
    public static class OtpProfileItem {
        private String identifier;
        private TimeToLive otpValidity;
        private String type;
        private int otpLength;
        private int maxAttempts = 3;
        private Map<String, Object> configuration = new HashMap<>();

        public OtpProfile toEntity(String domainId) {
            OtpProfile profile = new OtpProfile();
            profile.setConfiguration(configuration);
            profile.setDomainId(domainId);
            profile.setIdentifier(identifier);
            profile.setType(type);
            profile.setOtpValidity(otpValidity);
            profile.setOtpLength(otpLength);
            profile.setMaxAttempts(maxAttempts);
            return profile;
        }
    }
}
