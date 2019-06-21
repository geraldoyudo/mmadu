package com.mmadu.registration.typeconverters;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FieldTypeConverterResolver {
    private static FieldTypeConverterResolver resolver;

    private Map<String, Class<? extends AbstractFieldTypeConverter>> converterMap = new HashMap<>();

    private FieldTypeConverterResolver() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(DataFieldType.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(this.getClass().getPackage().getName())) {
            Class clazz = Class.forName(bd.getBeanClassName());
            Annotation annotation = clazz.getAnnotation(DataFieldType.class);
            Method name = annotation.annotationType().getMethod("name");
            converterMap.put((String) name.invoke(annotation), clazz);

        }
    }

    public Optional<Class<? extends AbstractFieldTypeConverter>> getConverterForType(String type) {
        return Optional.ofNullable(converterMap.get(type));
    }

    public static FieldTypeConverterResolver getInstance() throws Exception {
        if (resolver == null) {
            resolver = new FieldTypeConverterResolver();
        }
        return resolver;
    }
}
