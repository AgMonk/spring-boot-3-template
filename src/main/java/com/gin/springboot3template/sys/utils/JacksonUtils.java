package com.gin.springboot3template.sys.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * json工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 15:44
 */
public class JacksonUtils {
    private final static ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder()
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();

    public static void printPretty(Object obj) {
        try {
            final ObjectWriter prettyPrinter = MAPPER.writerWithDefaultPrettyPrinter();
            System.out.println(prettyPrinter.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}   
