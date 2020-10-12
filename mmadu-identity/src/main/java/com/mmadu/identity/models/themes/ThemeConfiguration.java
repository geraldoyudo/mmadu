package com.mmadu.identity.models.themes;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mmadu.identity.theme")
public class ThemeConfiguration {
    private String logoSvg;
    private String loginTitle;
    private ThemeColour themeColour = new ThemeColour();
}
