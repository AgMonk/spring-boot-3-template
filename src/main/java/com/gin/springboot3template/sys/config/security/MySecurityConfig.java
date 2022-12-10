package com.gin.springboot3template.sys.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

/**
 * SpringSecurity配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:53
 */
@Configuration
public class MySecurityConfig {
    /**
     * 接口文档放行
     */
    public static final List<String> DOC_WHITE_LIST = List.of("/doc.html","/webjars/**","/v3/api-docs/**");
    /**
     * 验证码放行
     */
    public static final List<String> VERIFY_CODE_WHITE_LIST = List.of("/sys/verifyCode/**");
    /**
     * 测试接口放行
     */
    public static final List<String> TEST_WHITE_LIST = List.of("/test/**");

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, DOC_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, VERIFY_CODE_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, TEST_WHITE_LIST.toArray(new String[0])).permitAll()
                .anyRequest().authenticated();

        http.csrf().disable();
        return http.build();
    }
}
