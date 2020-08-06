package com.mmadu.identity.controllers.resources;

import com.mmadu.identity.entities.DomainIdentityConfiguration;
import com.mmadu.identity.exceptions.DomainNotFoundException;
import com.mmadu.identity.models.themes.ThemeConfiguration;
import com.mmadu.identity.services.domain.DomainIdentityConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class DomainResourceController {
    private DomainIdentityConfigurationService domainIdentityConfigurationService;
    private ThemeConfiguration defaultConfiguration = new ThemeConfiguration();

    @Autowired
    public void setDomainIdentityConfigurationService(DomainIdentityConfigurationService domainIdentityConfigurationService) {
        this.domainIdentityConfigurationService = domainIdentityConfigurationService;
    }

    @Autowired
    public void setDefaultConfiguration(ThemeConfiguration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    @GetMapping("/themes/{domainId}/**")
    public String getResource(@PathVariable("domainId") String domainId,
                              HttpServletRequest request, Model model) {
        String path = request.getRequestURI().replace("/themes/" + domainId, "");
        DomainIdentityConfiguration configuration = domainIdentityConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
        model.addAttribute("theme", Optional.ofNullable(configuration.getThemeConfiguration()).orElse(defaultConfiguration));
        return "/themes" + path;
    }
}
