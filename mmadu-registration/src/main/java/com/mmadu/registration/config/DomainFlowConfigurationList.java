package com.mmadu.registration.config;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "mmadu.registration.domain-flow-config")
public class DomainFlowConfigurationList {
    private List<FieldTypeItem> fieldTypes = Collections.emptyList();
    @Size(min = 1)
    @NotNull
    private List<DomainItem> domains = Collections.emptyList();

    @Data
    public static class DomainItem {
        @NotEmpty
        private String domainId;
        @NotNull
        private List<RegistrationProfileItem> registrationProfiles;
        @NotNull
        @Size(min = 1)
        private List<FieldItem> fields;

        public DomainFlowConfiguration toEntity() {
            DomainFlowConfiguration configuration = new DomainFlowConfiguration();
            configuration.setDomainId(domainId);
            return configuration;
        }
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
            FieldType fieldType = new FieldType();
            fieldType.setId(id);
            fieldType.setName(name);
            fieldType.setMarkup(markup);
            fieldType.setFieldTypePattern(fieldTypePattern);
            fieldType.setType(type);
            fieldType.setEnclosingElement(enclosingElement);
            fieldType.setClasses(classes);
            fieldType.setStyle(style);
            fieldType.setScript(script);
            fieldType.setCss(css);
            fieldType.setMax(max);
            fieldType.setMin(min);
            return fieldType;
        }
    }

    @Data
    public static class FieldItem {
        @NotEmpty
        private String name;
        @NotEmpty
        private String code;
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
            Field field = new Field();
            field.setDomainId(domainId);
            field.setName(name);
            field.setCode(code);
            field.setPlaceholder(placeholder);
            field.setProperty(property);
            field.setFieldTypeId(fieldTypeId);
            field.setStyle(style);
            field.setLabel(label);
            field.setOrder(order);
            field.setPattern(pattern);
            field.setRequired(required);
            return field;
        }
    }

    @Data
    public static class RegistrationProfileItem {
        private String code;
        private String defaultRedirectUrl;
        private List<String> defaultRoles;
        private List<String> defaultAuthorities;
        private List<String> defaultGroups;
        private String headerOne;
        private String headerTwo;
        private String headerThree;
        private String instruction;
        private String submitButtonTitle;
        private List<String> fields;

        public RegistrationProfile toEntity(String domainId) {
            RegistrationProfile profile = new RegistrationProfile();
            profile.setDomainId(domainId);
            profile.setDefaultRedirectUrl(defaultRedirectUrl);
            profile.setDefaultRoles(defaultRoles);
            profile.setDefaultAuthorities(defaultAuthorities);
            profile.setHeaderOne(headerOne);
            profile.setHeaderTwo(headerTwo);
            profile.setHeaderThree(headerThree);
            profile.setInstruction(instruction);
            profile.setSubmitButtonTitle(submitButtonTitle);
            profile.setCode(code);
            profile.setFields(fields);
            profile.setDefaultGroups(defaultGroups);
            return profile;
        }
    }
}
