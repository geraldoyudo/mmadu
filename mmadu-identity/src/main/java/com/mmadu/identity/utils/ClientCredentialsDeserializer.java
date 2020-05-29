package com.mmadu.identity.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.identity.entities.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClientCredentialsDeserializer extends JsonDeserializer<ClientCredentials> {
    @Autowired
    private ClientCredentialsTypeRegistry registry;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ClientCredentials deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonNode treeNode = jsonParser.readValueAsTree();
            if (treeNode.isNull()) {
                return null;
            }
            String type = treeNode.get("type").asText();
            return objectMapper.treeToValue(treeNode.get("data"), registry.getCredentialType(type).orElseThrow(() -> new IllegalArgumentException("invalid credential type")));
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid dynamic object.", ex);
        }
    }
}
