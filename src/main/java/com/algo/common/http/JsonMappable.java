package com.algo.common.http;

import com.algo.common.singletons.ObjectMapperSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonMappable {

    public static <T> T fromJson(String json, Class<T> objectClass) {
        ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
        try {
            return objectMapper.readValue(json, objectClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toJson() {
        ObjectMapper objectMapper = ObjectMapperSingleton.getInstance();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
