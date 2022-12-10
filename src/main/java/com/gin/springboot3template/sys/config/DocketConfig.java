package com.gin.springboot3template.sys.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bx002
 */
@Configuration
public class DocketConfig {
    @Bean
    public GroupedOpenApi verifyCode() {
        return GroupedOpenApi.builder()
                .group("验证码")
                .pathsToMatch("/sys/verifyCode/**")
                .build();
    }

    @Bean
    public GroupedOpenApi test() {
        return GroupedOpenApi.builder()
                .group("测试")
                .pathsToMatch("/test/**")
                .build();
    }

}
