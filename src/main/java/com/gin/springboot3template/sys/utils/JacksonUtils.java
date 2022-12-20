package com.gin.springboot3template.sys.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Jackson工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 11:42
 */
public class JacksonUtils {
    private final static ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, Object> obj2Map(Object obj) {
        try {
            final String json = MAPPER.writeValueAsString(obj);
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);
            return MAPPER.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }
}   
