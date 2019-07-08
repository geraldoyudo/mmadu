package com.mmadu.registration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collections;

@Configuration
public class ThymeleafConfig {

    @Bean
    public ITemplateResolver domainComponentsResolver(
            @Value("${mmadu.registration.templates}") String templatesFolder) {
        SpringResourceTemplateResolver fileTemplateResolver = new SpringResourceTemplateResolver();
        fileTemplateResolver.setResolvablePatterns(Collections.singleton("domain/*"));
        fileTemplateResolver.setPrefix("file:" + templatesFolder + "/");
        fileTemplateResolver.setSuffix(".html");
        fileTemplateResolver.setCacheable(false);
        return fileTemplateResolver;
    }
}
