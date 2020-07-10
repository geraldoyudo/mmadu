package com.mmadu.registration.documentation;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.providers.RegistrationProfileFormFieldsManager;
import com.mmadu.registration.repositories.FieldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FieldsDocumentation extends AbstractDocumentation {
    @Autowired
    private FieldRepository fieldRepository;
    @MockBean
    private RegistrationProfileFormFieldsManager registrationProfileFormFieldsManager;

    @Test
    public void createFields() throws Exception {
        Field field = createNewField();
        mockMvc.perform(
                post("/repo/fields")
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.field.create"))
                        .content(objectMapper.writeValueAsString(field))
        ).andExpect(status().isCreated())
                .andDo(
                        document(DOCUMENTATION_NAME, requestFields(
                                fieldFields()
                        ))
                );
    }

    private static List<FieldDescriptor> fieldFields() {
        return asList(
                fieldWithPath("id").type("string").optional().description("Field ID"),
                fieldWithPath("pattern").description("Allowed string pattern for field"),
                fieldWithPath("style").description("css style for input element in field"),
                fieldWithPath("label").description("Input field label"),
                fieldWithPath("placeholder").description("input field placeholder"),
                fieldWithPath("fieldTypeId").description("Id of the field's type"),
                fieldWithPath("property").description("User property that the field will set"),
                fieldWithPath("order").description("Order of the field in Layout"),
                fieldWithPath("required").description("If the field input is required or not"),
                fieldWithPath("name").description("the form name of the field"),
                fieldWithPath("code").description("the field code"),
                fieldWithPath("domainId").description("the ID of the domain")
        );
    }

    private Field createNewField() {
        Field field = new Field();
        field.setPattern("");
        field.setStyle("");
        field.setLabel("Username");
        field.setPlaceholder("Enter Username");
        field.setFieldTypeId("1");
        field.setProperty("username");
        field.setOrder(1);
        field.setRequired(true);
        field.setName("username");
        field.setId("1");
        field.setDomainId("1");
        field.setCode("username");
        return field;
    }

    @Test
    public void getFieldById() throws Exception {
        Field field = fieldRepository.save(createNewField());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/fields/{fieldId}", field.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.global.field.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldIdParameter()
                        ), relaxedResponseFields(
                                fieldFields()
                        ))
                );
    }

    private ParameterDescriptor fieldIdParameter() {
        return parameterWithName("fieldId").description("The field ID");
    }

    @Test
    public void getFieldsByDomain() throws Exception {
        Field field = fieldRepository.save(createNewField());
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/repo/fields/search/findByDomainId")
                        .param("domainId", field.getDomainId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.field.read"))
        ).andExpect(status().isOk())
                .andDo(
                        document(DOCUMENTATION_NAME, requestParameters(
                                parameterWithName("domainId").description("Domain ID of the request")
                        ), relaxedResponseFields(
                                fieldListFields()
                        ))
                );
    }

    private static List<FieldDescriptor> fieldListFields() {
        return asList(
                fieldWithPath("_embedded.fields.[].pattern").description("Allowed string pattern for field"),
                fieldWithPath("_embedded.fields.[].style").description("css style for input element in field"),
                fieldWithPath("_embedded.fields.[].label").description("Input field label"),
                fieldWithPath("_embedded.fields.[].placeholder").description("input field placeholder"),
                fieldWithPath("_embedded.fields.[].fieldTypeId").description("Id of the field's type"),
                fieldWithPath("_embedded.fields.[].property").description("User property that the field will set"),
                fieldWithPath("_embedded.fields.[].order").description("Order of the field in Layout"),
                fieldWithPath("_embedded.fields.[].required").description("If the field input is required or not"),
                fieldWithPath("_embedded.fields.[].name").description("the form name of the field"),
                fieldWithPath("_embedded.fields.[].code").description("the field code"),
                fieldWithPath("_embedded.fields.[].domainId").description("the ID of the domain")
        );
    }

    @Test
    public void updateFieldById() throws Exception {
        final String modifiedPlaceHolder = "Enter a Username";
        Field field = fieldRepository.save(createNewField());
        mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/repo/fields/{fieldId}", field.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.field.update"))
                        .content(
                                objectMapper.createObjectNode()
                                        .put("placeholder", modifiedPlaceHolder)
                                        .toString()
                        )
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldIdParameter()
                        ))
                );
        assertThat(fieldRepository.findById(field.getId()).get().getPlaceholder(), equalTo(modifiedPlaceHolder));
    }

    @Test
    public void deleteFieldById() throws Exception {
        Field field = fieldRepository.save(createNewField());
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/repo/fields/{fieldId}", field.getId())
                        .header(HttpHeaders.AUTHORIZATION, authorization("a.1.field.delete"))
        ).andExpect(status().isNoContent())
                .andDo(
                        document(DOCUMENTATION_NAME, pathParameters(
                                fieldIdParameter()
                        ))
                );
        assertThat(fieldRepository.existsById(field.getId()), equalTo(false));
    }
}
