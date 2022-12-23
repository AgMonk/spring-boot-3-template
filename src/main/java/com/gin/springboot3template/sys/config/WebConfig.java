package com.gin.springboot3template.sys.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.springboot3template.sys.utils.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 项目全局配置类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 16:30
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonUtils.getMapper();
    }
}   
