package com.mmadu.registration.documentation;

import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.providers.DomainRegistrationFormFieldsManager;
import com.mmadu.registration.repositories.FieldTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FieldTypesDocumentation extends AbstractDocumentation {
    @Autowired
    private FieldTypeRepository fieldTypeRepository;
    @MockBean
    private DomainRegistrationFormFieldsManager domainRegistrationFormFieldsManager;

    @Test
    void createFieldTypes() throws Exception {
        FieldType fieldType = createNewFieldType();
        mockMvc.perform(
                post("/repo/fieldTypes")
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                        .content(objectMapper.writeValueAsString(fieldType))
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                fieldTypeFields()
                        ))
                );
    }

    private static List<FieldDescriptor> fieldTypeFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Field Type ID"),
                fieldWithPath("fieldTypePattern").description("Allowed string pattern for field type"),
                fieldWithPath("min").description("Minimum value for field type"),
                fieldWithPath("max").description("Maximum value for field type"),
                fieldWithPath("style").description("Style applied to all fields of the type"),
                fieldWithPath("type").description("Data type of to field type " +
                        "(integer, date, time, datetime, string, decimal)"),
                fieldWithPath("css").description("css for all fields with this type (can be overriden by field css)"),
                fieldWithPath("script").description("Script for field"),
                fieldWithPath("classes").description("css classes for field"),
                fieldWithPath("markup").description("HTML markup for field type"),
                fieldWithPath("name").description("The field type name"),
                fieldWithPath("enclosingElement").description("The element enclosing the field")
        );
    }

    private FieldType createNewFieldType() {
        FieldType fieldType = new FieldType();
        fieldType.setId("1");
        fieldType.setMin("10");
        fieldType.setMax("100");
        fieldType.setType("integer");
        fieldType.setCss("");
        fieldType.setScript("");
        fieldType.setClasses(Collections.singletonList("form-control"));
        fieldType.setFieldTypePattern("");
        fieldType.setMarkup("<label for='$field.name' class='sr-only'>$field.label</label>" +
                "<input type='number' id='$field.name' name='$field.name' class='form-control' " +
                "placeholder='$field.placeholder' $maxValue $minValue autofocus " +
                "$required $inputField $inputStyle $errorStyle >$errorDisplay");
        fieldType.setName("Age");
        fieldType.setEnclosingElement("div");
        return fieldType;
    }

    @Test
    void getFieldTypeById() throws Exception {
        FieldType fieldType = fieldTypeRepository.save(createNewFieldType());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/fieldTypes/{fieldTypeId}",
                        fieldType.getId())
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldTypeIdParameters()
                        ), relaxedResponseFields(
                                fieldTypeFields()
                        ))
                );
    }

    private ParameterDescriptor fieldTypeIdParameters() {
        return parameterWithName("fieldTypeId").description("The field type ID");
    }

    @Test
    void getAllFieldTypes() throws Exception {
        fieldTypeRepository.save(createNewFieldType());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/fieldTypes")
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, relaxedResponseFields(
                                fieldTypeListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> fieldTypeListFields() {
        return asList(
                fieldWithPath("_embedded.fieldTypes.[].fieldTypePattern")
                        .description("Allowed string pattern for field type"),
                fieldWithPath("_embedded.fieldTypes.[].min").description("Minimum value for field type"),
                fieldWithPath("_embedded.fieldTypes.[].max").description("Maximum value for field type"),
                fieldWithPath("_embedded.fieldTypes.[].type").description("Data type of to field type " +
                        "(integer, date, time, datetime, string, decimal)"),
                fieldWithPath("_embedded.fieldTypes.[].css")
                        .description("css for all fields with this type (can be overriden by field css)"),
                fieldWithPath("_embedded.fieldTypes.[].script").description("Script for field"),
                fieldWithPath("_embedded.fieldTypes.[].classes").description("css classes for field"),
                fieldWithPath("_embedded.fieldTypes.[].markup").description("HTML markup for field type"),
                fieldWithPath("_embedded.fieldTypes.[].name").description("The field type name"),
                fieldWithPath("_embedded.fieldTypes.[].enclosingElement").description("The element enclosing the field")
        );
    }

    @Test
    void updateFieldTypeById() throws Exception {
        final String modifiedName = "New Type";
        FieldType fieldType = fieldTypeRepository.save(createNewFieldType());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/repo/fieldTypes/{fieldTypeId}", fieldType.getId())
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
                        .content(
                                objectMapper.createObjectNode()
                                        .put("name", modifiedName)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldTypeIdParameters()
                        ))
                );
        assertThat(fieldTypeRepository.findById(fieldType.getId()).get().getName(), equalTo(modifiedName));
    }

    @Test
    void deleteFieldTypeById() throws Exception {
        FieldType fieldType = fieldTypeRepository.save(createNewFieldType());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/repo/fieldTypes/{fieldTypeId}", fieldType.getId())
                        .header(DOMAIN_AUTH_TOKEN_FIELD, ADMIN_TOKEN)
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldTypeIdParameters()
                        ))
                );
        assertThat(fieldTypeRepository.existsById(fieldType.getId()), equalTo(false));
    }
}
