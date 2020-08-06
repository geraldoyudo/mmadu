package com.mmadu.identity.models.themes;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mmadu.identity.theme")
public class ThemeConfiguration {
    private String iconSVG;
    private ThemeColour themeColour;
}
