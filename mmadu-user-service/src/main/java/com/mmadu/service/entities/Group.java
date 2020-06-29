package com.mmadu.service.entities;

import com.mmadu.security.api.DomainPayload;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Document
@CompoundIndexes({
        @CompoundIndex(name = "domain_group_identifier", def = "{'domainId': 1, 'identifier': 1}", unique = true),
        @CompoundIndex(name = "domain_group_name", def = "{'domainId': 1, 'name': 1}", unique = true)
})
public class Group implements DomainPayload {
    @Id
    private String id;
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    @NotEmpty(message = "identifier is required")
    private String identifier;
    @DBRef
    private Group parent;
    @DBRef
    private Set<Group> children = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
        if (parent != null) {
            this.parent.addChildren(this);
        }
    }

    public Set<Group> getChildren() {
        return children;
    }

    public void setChildren(Set<Group> children) {
        this.children = children;
    }

    public void addChildren(Group... groups) {
        this.children.addAll(Set.of(groups));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return new EqualsBuilder()
                .append(domainId, group.domainId)
                .append(name, group.name)
                .append(description, group.description)
                .append(identifier, group.identifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(domainId)
                .append(name)
                .append(description)
                .append(identifier)
                .toHashCode();
    }
}
