package com.mmadu.registration.controllers.resources;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.exceptions.DomainNotFoundException;
import com.mmadu.registration.models.themes.ThemeConfiguration;
import com.mmadu.registration.services.DomainFlowConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class DomainResourceController {
    private DomainFlowConfigurationService domainFlowConfigurationService;
    private ThemeConfiguration defaultConfiguration = new ThemeConfiguration();

    @Autowired
    public void setDomainFlowConfigurationService(DomainFlowConfigurationService domainFlowConfigurationService) {
        this.domainFlowConfigurationService = domainFlowConfigurationService;
    }

    @Autowired
    public void setDefaultConfiguration(ThemeConfiguration defaultConfiguration) {
        this.defaultConfiguration = defaultConfiguration;
    }

    @GetMapping("/themes/{domainId}/**")
    public String getResource(@PathVariable("domainId") String domainId,
                              HttpServletRequest request, Model model) {
        String path = request.getRequestURI().replace("/themes/" + domainId, "");
        DomainFlowConfiguration configuration = domainFlowConfigurationService.findByDomainId(domainId)
                .orElseThrow(DomainNotFoundException::new);
        model.addAttribute("theme", Optional.ofNullable(configuration.getTheme()).orElse(defaultConfiguration));
        return "/themes" + path;
    }
}
