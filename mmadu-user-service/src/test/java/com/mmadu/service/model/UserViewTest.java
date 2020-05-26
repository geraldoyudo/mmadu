package com.mmadu.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.models.UserView;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserViewTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialization() throws Exception {
        UserView userObject = new UserView();
        userObject.setPassword("password");
        userObject.setUsername("username");
        userObject.setProperty("property", "one");
        userObject.setRoles(asList("admin"));
        userObject.setAuthorities(asList("manage-users"));
        String jsonString = objectMapper.writeValueAsString(userObject);
        System.out.println(jsonString);
        JsonNode user = objectMapper.readTree(jsonString);
        assertThat(user.get("username").asText(), equalTo("username"));
        assertThat(user.get("password").asText(), equalTo("password"));
        assertThat(user.get("property").asText(), equalTo("one"));
        assertThat(user.get("authorities").toString(), equalTo("[\"manage-users\"]"));
        assertThat(user.get("roles").toString(), equalTo("[\"admin\"]"));
    }

    @Test
    public void testDeserialization() throws Exception {
        UserView object = objectMapper
                .readValue("{\"username\":\"username\",\"password\":\"password\"," +
                                "\"roles\":[\"admin\"],\"authorities\":[\"manage-users\"],\"property\":\"one\"}",
                UserView.class);
        assertThat(object.getPassword(), equalTo("password"));
        assertThat(object.getUsername(), equalTo("username"));
        assertThat(object.getProperty("property").get(), equalTo("one"));
        assertThat(object.getRoles(), equalTo(asList("admin")));
        assertThat(object.getAuthorities(), equalTo(asList("manage-users")));
    }

}