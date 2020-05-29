package com.mmadu.identity.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mmadu.identity.entities.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClientCredentialsSerializer extends JsonSerializer<ClientCredentials> {
    @Autowired
    private ClientCredentialsTypeRegistry registry;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void serialize(ClientCredentials credentials, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (credentials == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type",
                    registry.getTypeName(credentials).orElseThrow(() -> new IllegalStateException("Invalid credential type"))
            );
            jsonGenerator.writeFieldName("data");
            objectMapper.writeValue(jsonGenerator, credentials);
        }
    }
}
