package com.mmadu.registration.entities;

import com.mmadu.registration.models.registration.DefaultAccountStatus;
import com.mmadu.security.api.DomainPayload;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_code", def = "{'domainId': 1, 'code': 1}", unique = true)
})
public class RegistrationProfile implements DomainPayload {
    @Id
    private String id;
    @NotEmpty(message = "code is required")
    private String code;
    @Indexed(unique = true)
    private String domainId;
    private String defaultRedirectUrl;
    private String resourcesBaseUrl;
    private String formUrl;
    private List<String> defaultRoles;
    private List<String> defaultAuthorities;
    private List<String> defaultGroups;
    private DefaultAccountStatus defaultAccountStatus;
    @NotNull(message = "fields is required")
    @Size(min = 1, message = "fields cannot be empty")
    private List<String> fields;
    private String headerOne;
    private String headerTwo;
    private String headerThree;
    private String instruction;
    private String submitButtonTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
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

    public DefaultAccountStatus getDefaultAccountStatus() {
        return defaultAccountStatus;
    }

    public void setDefaultAccountStatus(DefaultAccountStatus defaultAccountStatus) {
        this.defaultAccountStatus = defaultAccountStatus;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RegistrationProfile that = (RegistrationProfile) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(code, that.code)
                .append(domainId, that.domainId)
                .append(defaultRedirectUrl, that.defaultRedirectUrl)
                .append(resourcesBaseUrl, that.resourcesBaseUrl)
                .append(formUrl, that.formUrl)
                .append(defaultRoles, that.defaultRoles)
                .append(defaultAuthorities, that.defaultAuthorities)
                .append(defaultGroups, that.defaultGroups)
                .append(defaultAccountStatus, that.defaultAccountStatus)
                .append(fields, that.fields)
                .append(headerOne, that.headerOne)
                .append(headerTwo, that.headerTwo)
                .append(headerThree, that.headerThree)
                .append(instruction, that.instruction)
                .append(submitButtonTitle, that.submitButtonTitle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(code)
                .append(domainId)
                .append(defaultRedirectUrl)
                .append(resourcesBaseUrl)
                .append(formUrl)
                .append(defaultRoles)
                .append(defaultAuthorities)
                .append(defaultGroups)
                .append(defaultAccountStatus)
                .append(fields)
                .append(headerOne)
                .append(headerTwo)
                .append(headerThree)
                .append(instruction)
                .append(submitButtonTitle)
                .toHashCode();
    }
}
