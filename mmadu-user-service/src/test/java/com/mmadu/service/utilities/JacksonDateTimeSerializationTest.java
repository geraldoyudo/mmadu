package com.mmadu.service.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmadu.service.config.SerializationConfig;
import com.mmadu.service.models.UserView;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@Import(SerializationConfig.class)
public class JacksonDateTimeSerializationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void serializeDate() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalDate.of(1990, 1, 1)),
                equalTo("\"1990-01-01\""));
    }

    @Test
    public void serializeTime() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalTime.of(1, 1, 0)),
                equalTo("\"01:01:00\""));
    }

    @Test
    public void serializeDateTime() throws Exception {
        assertThat(objectMapper.writeValueAsString(LocalDateTime.of(1990, 1, 1, 1, 1, 0)),
                equalTo("\"1990-01-01T01:01:00\""));
    }

    @Test
    public void serializeInMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", LocalDateTime.of(1990, 1, 1, 1, 1, 0));
        assertThat(objectMapper.writeValueAsString(map),
                equalTo("{\"key\":\"1990-01-01T01:01:00\"}"));
    }

    @Test
    public void serializeInUserView() throws Exception {
        UserView userView = new UserView();
        userView.setProperty("key", LocalDateTime.of(1990, 1, 1, 1, 1, 0));
        assertThat(objectMapper.writeValueAsString(userView),
                equalTo("{\"id\":null,\"username\":null,\"password\":null,\"roles\":[],\"authorities\":[],\"key\":\"1990-01-01T01:01:00\"}"));
    }
}
