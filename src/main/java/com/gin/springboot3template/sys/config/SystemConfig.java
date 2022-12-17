package com.gin.springboot3template.sys.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:11
 */
@Configuration
@ConfigurationProperties(prefix = "system")
@Getter
@Setter
public class SystemConfig {
    /**
     * 开放新用户注册
     */
    boolean newUser = true;

}
