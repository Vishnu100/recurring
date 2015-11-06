package com.itranswarp.recurring.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

/**
 * Created by changsure on 15/8/26.
 */
public class JsonUtil {

    public static <T> T jsonToEntity(String json, Class<T> typeClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json,typeClass);
    }

    public static String entityToJson(Object entity) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(entity);
    }
}
