package com.gorbatenko.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, Object> jsonToMap (String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, HashMap.class);
    }

    public static String getJsonValueAsString(String json, String key) throws JsonProcessingException {
        return jsonToMap(json).getOrDefault(key, null).toString();
    }

    public static Map<String, String> getJsonValueAsMap(String json, String key) throws JsonProcessingException {
        return (Map<String, String>) jsonToMap(json).getOrDefault(key, null);
    }

    public static boolean existsJsonKey (String json, String key) throws JsonProcessingException {
        return jsonToMap(json).containsKey(key);
    }


}
