package com.example.bs4bspringbackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonParser {

    // Private constructor to prevent instantiation
    private JsonParser() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(JsonParser.class);

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Problem serializing {}",(obj != null ? obj.getClass().getSimpleName() : "null") + ": ", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("Problem parsing JSON to {}", clazz.getSimpleName() + ": ", e);
            return null;
        }
    }
}
