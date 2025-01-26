package com.algo.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSingleton {

    private static final ObjectMapper INSTANCE = new ObjectMapper();

    private ObjectMapperSingleton() {
    }

    public static synchronized ObjectMapper getInstance() {
        return INSTANCE;
    }
}