package com.gin.springboot3template.sys.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurity配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:53
 */
@Configuration
public class MySecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().anyRequest().permitAll();

        http.csrf().disable();
        return http.build();
    }
}
