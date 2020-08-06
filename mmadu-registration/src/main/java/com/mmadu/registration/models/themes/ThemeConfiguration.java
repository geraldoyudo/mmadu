package com.mmadu.registration.models.themes;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmadu.registration.theme")
public class ThemeConfiguration {
    private String logoSvg;
    private ThemeColour themeColour = new ThemeColour();
}
