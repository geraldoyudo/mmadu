package com.mmadu.identity.entities;

import com.mmadu.identity.utils.TokenCategoryUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
@Document
@EqualsAndHashCode
public class Resource implements HasDomain {
    @Id
    private String id;
    @NotEmpty(message = "domainId is required")
    private String domainId;
    @NotEmpty(message = "identifier is required")
    private String identifier;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    @NotNull
    @Size(min = 1, message = "supportedTokenCategories should have at least one item")
    private List<String> supportedTokenCategories = Collections.singletonList(TokenCategoryUtils.CATEGORY_BEARER);
}
