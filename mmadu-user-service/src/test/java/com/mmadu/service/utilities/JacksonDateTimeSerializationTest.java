package com.mmadu.service.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.config.SerializationConfig;
import com.mmadu.service.models.UserView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = SerializationConfig.class)
public class JacksonDateTimeSerializationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeDate() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalDate.of(1990, 1, 1)),
                equalTo("\"1990-01-01\""));
    }

    @Test
    void serializeTime() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalTime.of(1, 1, 0)),
                equalTo("\"01:01:00\""));
    }

    @Test
    void serializeDateTime() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalDateTime.of(1990, 1, 1, 1, 1, 0)),
                equalTo("\"1990-01-01T01:01:00\""));
    }

    @Test
    void serializeInMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", LocalDateTime.of(1990, 1, 1, 1, 1, 0));
        assertThat(objectMapper.writeValueAsString(map),
                equalTo("{\"key\":\"1990-01-01T01:01:00\"}"));
    }

    @Test
    void serializeInUserView() throws Exception {
        UserView userView = new UserView();
        userView.setProperty("key", LocalDateTime.of(1990, 1, 1, 1, 1, 0));
        assertThat(objectMapper.writeValueAsString(userView),
                equalTo("{\"id\":null,\"username\":null,\"password\":null,\"roles\":[],\"authorities\":[],\"key\":\"1990-01-01T01:01:00\"}"));
    }
}
