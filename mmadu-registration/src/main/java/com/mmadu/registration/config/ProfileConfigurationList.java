package com.mmadu.registration.config;

import com.mmadu.registration.entities.RegistrationProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "mmadu.registration.profile-config")
@Data
public class ProfileConfigurationList {
    private List<RegistrationProfile> profiles = new LinkedList<>();
}
