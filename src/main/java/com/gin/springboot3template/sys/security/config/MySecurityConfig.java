package com.gin.springboot3template.sys.security.config;

import com.gin.springboot3template.sys.security.component.MyAuthenticationHandler;
import com.gin.springboot3template.sys.security.component.MyLoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;

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

    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 自定义RememberMe服务token持久化仓库
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource datasource) {
        final JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(datasource);
        //第一次启动的时候建表
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   MyLoginFilter loginFilter,
                                                   MyAuthenticationHandler authenticationHandler) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, DOC_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, VERIFY_CODE_WHITE_LIST.toArray(new String[0])).permitAll()
                .requestMatchers(HttpMethod.GET, TEST_WHITE_LIST.toArray(new String[0])).permitAll()
                .anyRequest().authenticated();


        //登陆
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        http.formLogin();

        //登出
        http.logout().logoutUrl("/sys/user/logout");

        //会话管理 引入依赖后已不再需要手动配置 sessionRegistry
        http.sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(authenticationHandler);

        //csrf验证 存储到Cookie中
        http.csrf().disable();
//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // 权限不足时的处理
        http.exceptionHandling().accessDeniedHandler(authenticationHandler);

        return http.build();
    }

}
