package com.mmadu.registration.providers;

import com.mmadu.registration.entities.Field;
import com.mmadu.registration.entities.FieldType;
import com.mmadu.registration.repositories.FieldRepository;
import com.mmadu.registration.repositories.FieldTypeRepository;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FormFieldsGeneratorImpl implements FormFieldsGenerator {
    private static final String FORM_FIELDS_PATH = "vm/form-fields.vm";

    private FieldRepository fieldRepository;
    private FieldTypeRepository fieldTypeRepository;
    private FieldMarkupGenerator fieldMarkupGenerator;
    private VelocityEngine velocityEngine;

    @Autowired
    public void setFieldRepository(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Autowired
    public void setFieldTypeRepository(FieldTypeRepository fieldTypeRepository) {
        this.fieldTypeRepository = fieldTypeRepository;
    }

    @Autowired
    public void setFieldMarkupGenerator(FieldMarkupGenerator fieldMarkupGenerator) {
        this.fieldMarkupGenerator = fieldMarkupGenerator;
    }

    @Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Override
    public String generateFormFieldsForDomain(String domainId) {
        List<Field> fields = fieldRepository.findByDomainId(domainId);
        Map<String, String> fieldIdTypeMap = generateFieldIdMap(fields);
        Map<String, FieldType> fieldTypeMap = generateFieldTypeIdMap(fieldIdTypeMap);
        List<String> fieldMarkups = generateFieldMarkups(fields, fieldTypeMap);
        List<String> scripts = generateScripts(fieldTypeMap.values());
        VelocityContext context = createContextFromMarkups(fieldMarkups, scripts);
        return generateStringOutputFromTemplateAndContext(context);
    }

    private Map<String, String> generateFieldIdMap(List<Field> fields) {
        Map<String, String> fieldIdTypeMap = new HashMap<>();
        fields.forEach(field -> fieldIdTypeMap.put(field.getId(), field.getFieldTypeId()));
        return fieldIdTypeMap;
    }

    private Map<String, FieldType> generateFieldTypeIdMap(Map<String, String> fieldIdTypeMap) {
        Map<String, FieldType> fieldTypeMap = new HashMap<>();
        fieldIdTypeMap.values().stream()
                .distinct()
                .forEach(typeId -> fieldTypeMap.put(typeId, fieldTypeRepository.findById(typeId).get()));
        return fieldTypeMap;
    }

    private List<String> generateFieldMarkups(List<Field> fields, Map<String, FieldType> fieldTypeMap) {
        return fields.stream()
                .map(field -> fieldMarkupGenerator.resolveField(field, fieldTypeMap.get(field.getFieldTypeId())))
                .collect(Collectors.toList());
    }

    private String generateStringOutputFromTemplateAndContext(VelocityContext context) {
        Template template = velocityEngine.getTemplate(FORM_FIELDS_PATH);
        StringWriter output = new StringWriter();
        template.merge(context, output);
        return output.getBuffer().toString();
    }

    private List<String> generateScripts(Collection<FieldType> types) {
        return types.stream()
                .map(type -> type.getScript())
                .collect(Collectors.toList());
    }

    private VelocityContext createContextFromMarkups(List<String> fieldMarkups, List<String> scripts) {
        VelocityContext context = new VelocityContext();
        context.put("fields", fieldMarkups);
        context.put("scripts", scripts);
        return context;
    }
}
