package com.gin.springboot3template.sys.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
    public static final List<String> DOC_WHITE_LIST = List.of("/doc.html", "/webjars/**", "/v3/api-docs/**");
    /**
     * 测试接口放行
     */
    public static final List<String> TEST_WHITE_LIST = List.of("/test/**");
    /**
     * 验证码放行
     */
    public static final List<String> VERIFY_CODE_WHITE_LIST = List.of("/sys/verifyCode/**");

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, DOC_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, VERIFY_CODE_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, TEST_WHITE_LIST.toArray(new String[0])).permitAll()
                .anyRequest().authenticated();


        http.formLogin().loginProcessingUrl("/sys/user/login");
        http.logout().logoutUrl("/sys/user/logout");


        http.csrf().disable();
        return http.build();
    }

    /**
     * 测试用用户数据源
     * @return 测试用用户数据源
     */
    @Bean
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
        users.createUser(User.withUsername("admin").password("{noop}123").roles("admin").build());
        return users;
    }
}
