package com.gin.springboot3template.sys.security.config;

import com.gin.springboot3template.sys.security.component.MyAuthenticationHandler;
import com.gin.springboot3template.sys.security.component.MyRememberMeServices;
import com.gin.springboot3template.sys.security.service.MyUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

/**
 * SpringSecurity配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:53
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
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


    private final MyUserDetailsServiceImpl myUserDetailsService;
    private final DataSource datasource;

    private final MyAuthenticationHandler handler = new MyAuthenticationHandler();


    /**
     * 自定义RememberMe服务token持久化仓库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(datasource);
        //第一次启动的时候建表
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * 记住我服务实现
     * @return 记住我服务实现
     */
    @Bean
    public MyRememberMeServices rememberMeServices() {
        return new MyRememberMeServices(UUID.randomUUID().toString(), myUserDetailsService, persistentTokenRepository());
    }


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
}
