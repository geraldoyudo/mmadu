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

@ConfigurationProperties(prefix = "mmadu.domain-otp-config")
public class DomainOtpConfigurationList {
    private List<DomainItem> domains = Collections.emptyList();

    public List<DomainItem> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainItem> domains) {
        this.domains = domains;
    }

    public static class DomainItem {
        @NotEmpty
        private String domainId;
        private String jwkSetUri;
        @Size(min = 1)
        private List<String> supportedProviders = Collections.singletonList("alphanumeric");
        @Size(min = 1)
        private List<OtpProfileItem> profiles;

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public String getJwkSetUri() {
            return jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        public List<String> getSupportedProviders() {
            return supportedProviders;
        }

        public void setSupportedProviders(List<String> supportedProviders) {
            this.supportedProviders = supportedProviders;
        }

        public List<OtpProfileItem> getProfiles() {
            return profiles;
        }

        public void setProfiles(List<OtpProfileItem> profiles) {
            this.profiles = profiles;
        }

        public DomainOtpConfiguration toEntity() {
            DomainOtpConfiguration configuration = new DomainOtpConfiguration();
            configuration.setDomainId(domainId);
            configuration.setSupportedProviders(supportedProviders);
            configuration.setJwkSetUri(jwkSetUri);
            return configuration;
        }
    }

    public static class OtpProfileItem {
        private String identifier;
        private TimeToLive otpValidity;
        private String type;
        private int otpLength;
        private int maxAttempts = 3;
        private Map<String, Object> configuration = new HashMap<>();

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public TimeToLive getOtpValidity() {
            return otpValidity;
        }

        public void setOtpValidity(TimeToLive otpValidity) {
            this.otpValidity = otpValidity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getOtpLength() {
            return otpLength;
        }

        public void setOtpLength(int otpLength) {
            this.otpLength = otpLength;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public Map<String, Object> getConfiguration() {
            return configuration;
        }

        public void setConfiguration(Map<String, Object> configuration) {
            this.configuration = configuration;
        }

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
