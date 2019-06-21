package com.mmadu.registration.typeconverters;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface DataFieldType {
    String name();
}