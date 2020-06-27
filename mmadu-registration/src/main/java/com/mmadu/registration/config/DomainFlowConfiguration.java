package com.mmadu.registration.config;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "mmadu.registration.domain-flow-config")
public class DomainFlowConfiguration {
    private List<FieldTypeItem> fieldTypes = Collections.emptyList();
    @Size(min = 1)
    @NotNull
    private List<DomainItem> domains = Collections.emptyList();

    @Data
    public static class DomainItem {
        @NotEmpty
        private String domainId;
        @NotNull
        private RegistrationProfileItem registrationProfile;
        @NotNull
        @Size(min = 1)
        private List<FieldItem> fields;
    }

    @Data
    public static class FieldTypeItem {
        @NotEmpty
        private String id;
        @NotEmpty
        private String name;
        private String markup;
        private String fieldTypePattern;
        private String type;
        private String enclosingElement = "div";
        private List<String> classes;
        private String style;
        private String script;
        private String css;
        private String max;
        private String min;

        public FieldType toEntity() {
            return FieldType.builder()
                    .id(id)
                    .name(name)
                    .markup(markup)
                    .fieldTypePattern(fieldTypePattern)
                    .type(type)
                    .enclosingElement(enclosingElement)
                    .classes(classes)
                    .style(style)
                    .script(script)
                    .css(css)
                    .max(max)
                    .min(min)
                    .build();
        }
    }

    @Data
    public static class FieldItem {
        @NotEmpty
        private String name;
        @NotEmpty
        private String placeholder;
        @NotEmpty
        private String property;
        @NotEmpty
        private String fieldTypeId;
        @NotNull
        private String style;
        @NotEmpty
        private String label;
        private int order;
        private String pattern;
        boolean required;

        public Field toEntity(String domainId) {
            return Field.builder()
                    .domainId(domainId)
                    .name(name)
                    .placeholder(placeholder)
                    .property(property)
                    .fieldTypeId(fieldTypeId)
                    .style(style)
                    .label(label)
                    .order(order)
                    .pattern(pattern)
                    .required(required)
                    .build();
        }
    }

    @Data
    public static class RegistrationProfileItem {
        private String defaultRedirectUrl;
        private List<String> defaultRoles;
        private List<String> defaultAuthorities;
        private String headerOne;
        private String headerTwo;
        private String headerThree;
        private String instruction;
        private String submitButtonTitle;

        public RegistrationProfile toEntity(String domainId) {
            return RegistrationProfile.builder()
                    .domainId(domainId)
                    .defaultRedirectUrl(defaultRedirectUrl)
                    .defaultRoles(defaultRoles)
                    .defaultAuthorities(defaultAuthorities)
                    .headerOne(headerOne)
                    .headerTwo(headerTwo)
                    .headerThree(headerThree)
                    .instruction(instruction)
                    .submitButtonTitle(submitButtonTitle)
                    .build();
        }
    }
}
