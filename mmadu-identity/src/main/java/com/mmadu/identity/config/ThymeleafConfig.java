package com.mmadu.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collections;

@Configuration
public class ThymeleafConfig {

    @Bean
    public ITemplateResolver domainComponentsResolver() {
        SpringResourceTemplateResolver fileTemplateResolver = new SpringResourceTemplateResolver();
        fileTemplateResolver.setResolvablePatterns(Collections.singleton("/themes/**"));
        fileTemplateResolver.setPrefix("classpath:");
        fileTemplateResolver.setSuffix(".css");
        fileTemplateResolver.setCacheable(true);
        return fileTemplateResolver;
    }
}
