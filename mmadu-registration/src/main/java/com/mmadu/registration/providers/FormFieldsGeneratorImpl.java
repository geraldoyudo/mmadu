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
        Map<String, String> fieldIdTypeMap = new HashMap<>();
        fields.forEach(field -> fieldIdTypeMap.put(field.getId(), field.getFieldTypeId()));
        Map<String, FieldType> fieldTypeMap = new HashMap<>();
        fieldIdTypeMap.values().stream()
                .distinct()
                .forEach(typeId -> fieldTypeMap.put(typeId, fieldTypeRepository.findById(typeId).get()));
        List<String> fieldMarkups = fields.stream()
                .map(field -> fieldMarkupGenerator.resolveField(field, fieldTypeMap.get(field.getFieldTypeId())))
                .collect(Collectors.toList());
        VelocityContext context = new VelocityContext();
        context.put("fields", fieldMarkups);
        Template template = velocityEngine.getTemplate(FORM_FIELDS_PATH);
        StringWriter output = new StringWriter();
        template.merge(context, output);
        return output.getBuffer().toString();
    }
}
