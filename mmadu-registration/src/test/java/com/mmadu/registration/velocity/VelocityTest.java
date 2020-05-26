package com.mmadu.registration.velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class VelocityTest {
    private static VelocityEngine engine;

    @BeforeAll
    public static void setUpClass() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine = new VelocityEngine();
        engine.init(p);
    }

    @Test
    public void getTemplate() {
        Template template = engine.getTemplate("templates/sample-template.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        assertThat(writer.getBuffer().toString(), equalTo("Hello World!!"));
    }
}
