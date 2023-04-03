package com.gin.springboot3template.sys.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

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
public class SystemProperties implements Serializable {
    /**
     * 文件根目录,本系统管理的文件将全部放在该目录下
     */
    String homePath;
    /**
     * 注册功能开放 关闭后只能由管理员创建新用户
     */
    boolean newUser = true;
    /**
     * 解析json时是否在遇到未知字段时报错
     */
    boolean failOnUnknownProperties = true;
}
