package com.mmadu.registration.config;

import com.mmadu.registration.entities.DomainFlowConfiguration;
import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.entities.RegistrationProfile;
import com.mmadu.registration.models.FieldOptions;
import com.mmadu.registration.models.PasswordResetFlowConfiguration;
import com.mmadu.registration.models.registration.DefaultAccountStatus;
import com.mmadu.registration.models.themes.ThemeConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "mmadu.registration.domain-flow-config")
public class DomainFlowConfigurationList {
    private List<FieldTypeItem> fieldTypes = Collections.emptyList();
    @Size(min = 1)
    @NotNull
    private List<DomainItem> domains = Collections.emptyList();

    public List<FieldTypeItem> getFieldTypes() {
        return fieldTypes;
    }

    public void setFieldTypes(List<FieldTypeItem> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    public List<DomainItem> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainItem> domains) {
        this.domains = domains;
    }

    public static class DomainItem {
        @NotEmpty
        private String domainId;
        private String jwkSetUri;
        @NotNull
        private List<RegistrationProfileItem> registrationProfiles;
        @NotNull
        @Size(min = 1)
        private List<FieldItem> fields;
        @NotNull
        private ThemeConfiguration theme = new ThemeConfiguration();
        private PasswordResetFlowConfiguration passwordReset = new PasswordResetFlowConfiguration();

        public String getDomainId() {
            return domainId;
        }

        public void setDomainId(String domainId) {
            this.domainId = domainId;
        }

        public String getJwkSetUri() {
            return jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        public List<RegistrationProfileItem> getRegistrationProfiles() {
            return registrationProfiles;
        }

        public void setRegistrationProfiles(List<RegistrationProfileItem> registrationProfiles) {
            this.registrationProfiles = registrationProfiles;
        }

        public List<FieldItem> getFields() {
            return fields;
        }

        public void setFields(List<FieldItem> fields) {
            this.fields = fields;
        }

        public ThemeConfiguration getTheme() {
            return theme;
        }

        public void setTheme(ThemeConfiguration theme) {
            this.theme = theme;
        }

        public PasswordResetFlowConfiguration getPasswordReset() {
            return passwordReset;
        }

        public void setPasswordReset(PasswordResetFlowConfiguration passwordReset) {
            this.passwordReset = passwordReset;
        }

        public DomainFlowConfiguration toEntity() {
            DomainFlowConfiguration configuration = new DomainFlowConfiguration();
            configuration.setDomainId(domainId);
            configuration.setTheme(theme);
            configuration.setJwkSetUri(jwkSetUri);
            configuration.setPasswordReset(passwordReset);
            return configuration;
        }
    }

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
        private List<FieldOptions> options;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMarkup() {
            return markup;
        }

        public void setMarkup(String markup) {
            this.markup = markup;
        }

        public String getFieldTypePattern() {
            return fieldTypePattern;
        }

        public void setFieldTypePattern(String fieldTypePattern) {
            this.fieldTypePattern = fieldTypePattern;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEnclosingElement() {
            return enclosingElement;
        }

        public void setEnclosingElement(String enclosingElement) {
            this.enclosingElement = enclosingElement;
        }

        public List<String> getClasses() {
            return classes;
        }

        public void setClasses(List<String> classes) {
            this.classes = classes;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public String getCss() {
            return css;
        }

        public void setCss(String css) {
            this.css = css;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public List<FieldOptions> getOptions() {
            return options;
        }

        public void setOptions(List<FieldOptions> options) {
            this.options = options;
        }

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
            fieldType.setOptions(options);
            return fieldType;
        }
    }

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
        private boolean required;
        private boolean unique;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getFieldTypeId() {
            return fieldTypeId;
        }

        public void setFieldTypeId(String fieldTypeId) {
            this.fieldTypeId = fieldTypeId;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public boolean isUnique() {
            return unique;
        }

        public void setUnique(boolean unique) {
            this.unique = unique;
        }

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
            field.setUnique(unique);
            return field;
        }
    }

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
        private String resourcesBaseUrl;
        private String formUrl;
        private DefaultAccountStatus defaultAccountStatus;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDefaultRedirectUrl() {
            return defaultRedirectUrl;
        }

        public void setDefaultRedirectUrl(String defaultRedirectUrl) {
            this.defaultRedirectUrl = defaultRedirectUrl;
        }

        public List<String> getDefaultRoles() {
            return defaultRoles;
        }

        public void setDefaultRoles(List<String> defaultRoles) {
            this.defaultRoles = defaultRoles;
        }

        public List<String> getDefaultAuthorities() {
            return defaultAuthorities;
        }

        public void setDefaultAuthorities(List<String> defaultAuthorities) {
            this.defaultAuthorities = defaultAuthorities;
        }

        public List<String> getDefaultGroups() {
            return defaultGroups;
        }

        public void setDefaultGroups(List<String> defaultGroups) {
            this.defaultGroups = defaultGroups;
        }

        public String getHeaderOne() {
            return headerOne;
        }

        public void setHeaderOne(String headerOne) {
            this.headerOne = headerOne;
        }

        public String getHeaderTwo() {
            return headerTwo;
        }

        public void setHeaderTwo(String headerTwo) {
            this.headerTwo = headerTwo;
        }

        public String getHeaderThree() {
            return headerThree;
        }

        public void setHeaderThree(String headerThree) {
            this.headerThree = headerThree;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public String getSubmitButtonTitle() {
            return submitButtonTitle;
        }

        public void setSubmitButtonTitle(String submitButtonTitle) {
            this.submitButtonTitle = submitButtonTitle;
        }

        public List<String> getFields() {
            return fields;
        }

        public void setFields(List<String> fields) {
            this.fields = fields;
        }

        public DefaultAccountStatus getDefaultAccountStatus() {
            return defaultAccountStatus;
        }

        public void setDefaultAccountStatus(DefaultAccountStatus defaultAccountStatus) {
            this.defaultAccountStatus = defaultAccountStatus;
        }

        public String getResourcesBaseUrl() {
            return resourcesBaseUrl;
        }

        public void setResourcesBaseUrl(String resourcesBaseUrl) {
            this.resourcesBaseUrl = resourcesBaseUrl;
        }

        public String getFormUrl() {
            return formUrl;
        }

        public void setFormUrl(String formUrl) {
            this.formUrl = formUrl;
        }

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
            profile.setDefaultAccountStatus(defaultAccountStatus);
            profile.setResourcesBaseUrl(resourcesBaseUrl);
            profile.setFormUrl(formUrl);
            return profile;
        }
    }
}
